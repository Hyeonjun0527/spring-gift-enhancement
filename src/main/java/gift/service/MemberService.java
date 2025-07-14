package gift.service;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberRegisterRequestDto;
import gift.entity.Member;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.InvalidLoginException;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String register(MemberRegisterRequestDto requestDto) {

        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new EmailAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(requestDto.password());

        Member member = new Member(requestDto.email(), encodedPassword);

        Member saved = memberRepository.save(member);

        return jwtProvider.createToken(saved);
    }

    public String login(MemberLoginRequestDto requestDto) {

        Member member = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(requestDto.password(), member.getPassword())) {

            throw new InvalidLoginException();
        }

        return jwtProvider.createToken(member);
    }
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다. id=" + id));
    }
    public Long parseTokenAndGetMemberId(String token) {

        String subject = jwtProvider.getSubject(token);
        return Long.parseLong(subject);


    }
}