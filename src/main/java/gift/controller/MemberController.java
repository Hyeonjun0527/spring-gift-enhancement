package gift.controller;

import gift.dto.LoginRequest;
import gift.dto.RegisterRequest;
import gift.dto.TokenResponse;
import gift.model.Member;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> addProduct(@Valid @RequestBody RegisterRequest request) {
        TokenResponse accessToken = memberService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(accessToken);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> addProduct(@Valid @RequestBody LoginRequest request) {
        TokenResponse accessToken = memberService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(accessToken);
    }
}
