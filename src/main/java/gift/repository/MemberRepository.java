package gift.repository;

import gift.model.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert insert;

  public MemberRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    insert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("member")
        .usingGeneratedKeyColumns("id");
  }

  public Member save(Member member){
    Map<String, Object> params = new HashMap<>();
    params.put("email",member.getEmail());
    params.put("password",member.getPassword());
    Number key = insert.executeAndReturnKey(params);
    member.setId(key.longValue());
    return member;
  }

  public Optional<Member> findByEmail(String email){
    String sql = "select * from member where email = ?";
    return jdbcTemplate.query(sql, this::mapRow, email).stream().findFirst();
  }

  public List<Member> findAll(){
    String sql = "select * from member";
    return jdbcTemplate.query(sql, this::mapRow);
  }

  private Member mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new Member(
        rs.getLong("id"),
        rs.getString("email"),
        rs.getString("password")
    );
  }
}
