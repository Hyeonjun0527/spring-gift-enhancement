package gift.repository;

import gift.entity.Wishlist;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class WishlistRepository {
    private final JdbcClient jdbcClient;

    public WishlistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void save(Wishlist wishlist) {
        jdbcClient.sql("INSERT INTO wishlist (member_id, product_id, quantity) " +
                        "VALUES (:memberId, :productId, :quantity)")
                .param("memberId", wishlist.getMemberId())
                .param("productId", wishlist.getProductId())
                .param("quantity", wishlist.getQuantity())
                .update();
    }


    public List<Wishlist> findByMemberId(Long memberId) {
        return jdbcClient.sql(
                        "SELECT id, member_id, product_id, quantity " +
                                "  FROM wishlist " +
                                " WHERE member_id = :memberId")
                .param("memberId", memberId)
                .query((rs, rowNum) -> mapRow(rs))
                .list();
    }


    public Optional<Wishlist> findByMemberIdAndProductId(Long memberId, Long productId) {
        return jdbcClient.sql(
                        "SELECT id, member_id, product_id, quantity " +
                                "  FROM wishlist " +
                                " WHERE member_id = :memberId " +
                                "   AND product_id = :productId")
                .param("memberId", memberId)
                .param("productId", productId)
                .query((rs, rowNum) -> mapRow(rs))
                .optional();
    }


    public void deleteByMemberIdAndProductId(Long memberId, Long productId) {
        jdbcClient.sql("DELETE FROM wishlist WHERE member_id = :memberId AND product_id = :productId")
                .param("memberId", memberId)
                .param("productId", productId)
                .update();
    }

    private Wishlist mapRow(ResultSet rs) throws SQLException {
        return new Wishlist(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity")
        );
    }

    public void update(Wishlist wishlist) {
        jdbcClient.sql("UPDATE wishlist SET quantity = :quantity WHERE member_id = :memberId AND product_id = :productId")
                .param("memberId", wishlist.getMemberId())
                .param("productId", wishlist.getProductId())
                .param("quantity", wishlist.getQuantity())
                .update();
    }
    public void upsertWishlist(Long memberId, Long productId) {
        jdbcClient.sql("""
        MERGE INTO wishlist (member_id, product_id, quantity)
        KEY (member_id, product_id)
        VALUES (
          :memberId,
          :productId,
          COALESCE(
            (SELECT quantity
               FROM wishlist
              WHERE member_id = :memberId
                AND product_id = :productId
            ) + 1,
            1
          )
        )
        """)
                .param("memberId", memberId)
                .param("productId", productId)
                .update();
    }



}