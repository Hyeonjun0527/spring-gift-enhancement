package gift.repository;

import gift.entity.Member;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public Member save(Member member) {
        jdbcClient.sql("INSERT INTO members (email, password) VALUES (:email, :password)")
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .update();

        return findByEmail(member.getEmail()).orElseThrow();
    }


    public Optional<Member> findByEmail(String email) {
        return jdbcClient.sql("SELECT * FROM members WHERE email = :email")
                .param("email", email)
                .query((rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password")
                ))
                .optional();
    }
        public Optional<Member> findById(Long id) {
            return jdbcClient.sql(
                            "SELECT id, email, password FROM members WHERE id = :id")
                    .param("id", id)
                    .query((rs, rowNum) -> new Member(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password")
                    ))
                    .optional();
        }
    }