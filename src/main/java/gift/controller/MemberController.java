package gift.controller;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberLoginResponseDto;
import gift.dto.MemberRegisterRequestDto;
import gift.dto.MemberRegisterResponseDto;
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

    public final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponseDto> registerMember(
            @RequestBody @Valid MemberRegisterRequestDto requestDto) {

        String token = memberService.register(requestDto);
        MemberRegisterResponseDto responseDto = new MemberRegisterResponseDto(token);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseDto> loginMember(
            @RequestBody @Valid MemberLoginRequestDto requestDto) {

        String token = memberService.login(requestDto);
        MemberLoginResponseDto responseDto = new MemberLoginResponseDto(token);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
