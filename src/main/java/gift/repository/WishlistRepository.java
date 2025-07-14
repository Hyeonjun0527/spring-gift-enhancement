package gift.repository;

import gift.dto.WishResponse;
import gift.model.Member;
import gift.model.Wishlist;
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
public class WishlistRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public WishlistRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("wishlist")
            .usingGeneratedKeyColumns("id");
    }

    public Wishlist save(Wishlist wishlist) {
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("memberId", wishlist.getMemberId())
            .addValue("productId", wishlist.getProductId())
            .addValue("quantity", wishlist.getQuantity());
        Long newId = jdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Wishlist(newId, wishlist.getMemberId(), wishlist.getProductId(),
            wishlist.getQuantity());
    }

    public List<Wishlist> findAll(Long memberId) {
        String sql = "SELECT id, memberId, productId, quantity FROM WISHLIST WHERE memberId = ?";
        return jdbcTemplate.query(sql, wishListRowMapper(), memberId);
    }

    public Optional<Wishlist> findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT id, memberId, productId, quantity FROM WISHLIST WHERE memberId = ? and productId = ?";
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, wishListRowMapper(), memberId, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM WISHLIST WHERE memberId = ? and productId = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    // DB 초기화
    public void deleteAll() {
        String sql = "DELETE FROM MEMBER";
        jdbcTemplate.update(sql);
    }

    private RowMapper<Wishlist> wishListRowMapper() {
        return ((rs, rowNum) -> {
            Long id = rs.getLong("id");
            Long memberId = rs.getLong("memberId");
            Long productId = rs.getLong("productId");
            int quantity = rs.getInt("quantity");
            return new Wishlist(id, memberId, productId, quantity);
        });
    }

}
