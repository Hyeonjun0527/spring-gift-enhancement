package gift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.LoginMemberDto;
import gift.dto.RegisterRequest;
import gift.dto.TokenResponse;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.exception.MemberNotFoundException;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.repository.WishlistRepository;
import gift.service.MemberService;
import gift.service.WishlistService;
import java.util.Optional;
import jdk.jshell.spi.ExecutionControlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class WishlistControllerTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        wishlistRepository.deleteAll();
        memberRepository.deleteAll(); // 테스트마다 DB 초기화
    }

    @Test
    void 위시리스트_추가() throws Exception {
        TokenResponse tokenResponse = memberService.save(
            new RegisterRequest("abc@naver.com", "1234"));
        Long memberId = memberRepository.findByEmail("abc@naver.com").get().getId();

        WishRequest wishRequest = new WishRequest(1L, 3);
        String json = objectMapper.writeValueAsString(wishRequest);

        mockMvc.perform(post("/api/wishes").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenResponse.getToken()).content(json))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.memberId").value(memberId))
            .andExpect(jsonPath("$.productId").value(1)).andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void 위시리스트_없는_상품_추가() throws Exception {
        TokenResponse tokenResponse = memberService.save(
            new RegisterRequest("abc@naver.com", "1234"));
        Long memberId = memberRepository.findByEmail("abc@naver.com").get().getId();

        WishRequest wishRequest = new WishRequest(100L, 3);
        String json = objectMapper.writeValueAsString(wishRequest);

        mockMvc.perform(post("/api/wishes").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenResponse.getToken()).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("id: 100. 해당 ID의 상품이 존재하지 않습니다."));
    }

    @Test
    void 토큰_없이_위시리스트_추가_요청() throws Exception {
        TokenResponse tokenResponse = memberService.save(
            new RegisterRequest("abc@naver.com", "1234"));

        WishRequest wishRequest = new WishRequest(1L, 3);
        String json = objectMapper.writeValueAsString(wishRequest);

        mockMvc.perform(post("/api/wishes").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isUnauthorized()).andExpect(content().string("Token is missing"));
    }

    @Test
    void 잘못된_토큰_위시리스트_추가_요청() throws Exception {
        TokenResponse tokenResponse = new TokenResponse("Invalid Token");

        WishRequest wishRequest = new WishRequest(1L, 3);
        String json = objectMapper.writeValueAsString(wishRequest);

        mockMvc.perform(post("/api/wishes").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenResponse.getToken()).content(json))
            .andExpect(status().isUnauthorized()).andExpect(content().string("Invalid Token"));
    }

    @Test
    void 위시리스트_추가_수량이_음수() throws Exception {
        TokenResponse tokenResponse = memberService.save(
            new RegisterRequest("abc@naver.com", "1234"));
        Long memberId = memberRepository.findByEmail("abc@naver.com").get().getId();

        WishRequest wishRequest = new WishRequest(1L, -3);
        String json = objectMapper.writeValueAsString(wishRequest);

        mockMvc.perform(post("/api/wishes").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenResponse.getToken()).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.messages[0]").value("수량은 음수가 될 수 없습니다."));
    }

    @Test
    void 위시리스트_전체_조회() throws Exception {
        TokenResponse tokenResponse = memberService.save(
            new RegisterRequest("abc@naver.com", "1234"));
        wishlistService.create(new WishRequest(1L, 3), new LoginMemberDto("abc@naver.com"));
        wishlistService.create(new WishRequest(3L, 10), new LoginMemberDto("abc@naver.com"));

        Long memberId = memberRepository.findByEmail("abc@naver.com").get().getId();

        mockMvc.perform(get("/api/wishes").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenResponse.getToken())).andExpect(status().isOk())
            .andExpect(jsonPath("$[0].memberId").value(memberId))
            .andExpect(jsonPath("$[0].productId").value(1))
            .andExpect(jsonPath("$[0].quantity").value(3))
            .andExpect(jsonPath("$[1].memberId").value(memberId))
            .andExpect(jsonPath("$[1].productId").value(3))
            .andExpect(jsonPath("$[1].quantity").value(10));
    }

    @Test
    void 위시리스트_삭제() throws Exception {
        TokenResponse tokenResponse = memberService.save(
            new RegisterRequest("abc@naver.com", "1234"));

        wishlistService.create(new WishRequest(1L, 3), new LoginMemberDto("abc@naver.com"));

        mockMvc.perform(delete("/api/wishes/1").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", tokenResponse.getToken())).andExpect(status().isNoContent());
    }

    @Test
    void 존재하지_않는_위시리스트_삭제() throws Exception {
        TokenResponse tokenResponse = memberService.save(
            new RegisterRequest("abc@naver.com", "1234"));

        mockMvc.perform(delete("/api/wishes/1").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", tokenResponse.getToken())).andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("삭제할 위시리스트가 존재하지 않습니다."));
    }
}
