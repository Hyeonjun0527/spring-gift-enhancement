package gift.repository;

import gift.model.Member;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    // 회원가입
    public Member save(Member member) {
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("email", member.getEmail())
            .addValue("password", member.getPassword());
        Long newId = jdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Member(newId, member.getEmail(), member.getPassword());
    }

    public List<Member> findAll(){
        String sql = "SELECT id, email, password FROM MEMBER";
        return jdbcTemplate.query(sql, memberRowMapper());
    }

    // 멤버 조회
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM MEMBER WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper(), email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // DB 초기화
    public void deleteAll() {
        String sql = "DELETE FROM MEMBER";
        jdbcTemplate.update(sql);
    }

    private RowMapper<Member> memberRowMapper() {
        return ((rs, rowNum) -> {
            Long id = rs.getLong("id");
            String email = rs.getString("email");
            String password = rs.getString("password");
            return new Member(id, email, password);
        });
    }
}
