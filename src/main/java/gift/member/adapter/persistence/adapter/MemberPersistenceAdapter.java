package gift.member.adapter.persistence.adapter;

import gift.member.adapter.persistence.mapper.MemberPersistenceMapper;
import gift.member.adapter.persistence.repository.MemberJpaRepository;
import gift.member.domain.model.Member;
import gift.member.domain.port.out.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberPersistenceAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberPersistenceMapper memberMapper;

    public MemberPersistenceAdapter(MemberJpaRepository memberJpaRepository, MemberPersistenceMapper memberMapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    public Member save(Member member) {
        var entity = memberJpaRepository.save(memberMapper.toEntity(member));
        return memberMapper.toDomain(entity);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .map(memberMapper::toDomain);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id)
                .map(memberMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return memberJpaRepository.existsById(id);
    }

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll().stream()
                .map(memberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        memberJpaRepository.deleteById(id);
    }
} 