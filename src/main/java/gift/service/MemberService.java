package gift.service;

import gift.dto.LoginRequest;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.InvalidPasswordException;
import gift.exception.MemberNotFoundException;
import gift.util.JwtUtil;
import gift.util.PasswordUtil;
import gift.dto.RegisterRequest;
import gift.dto.TokenResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponse save(RegisterRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        String encryptedPassword = PasswordUtil.encode(request.getPassword());

        Member member = new Member(request.getEmail(), encryptedPassword);
        Member savedMember = memberRepository.save(member);

        String accessToken = jwtUtil.generateAccessToken(savedMember);

        return new TokenResponse(accessToken);
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(() ->
            new MemberNotFoundException(request.getEmail()));

        if (!PasswordUtil.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtUtil.generateAccessToken(member);

        return new TokenResponse(accessToken);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member findByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            throw new MemberNotFoundException(email);
        }
        return member.get();
    }

}
