package gift.repository;

import gift.model.Product;
import gift.model.WishItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class WishlistRepository {
  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert insert;

  public WishlistRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.insert = new  SimpleJdbcInsert(jdbcTemplate).withTableName("wishlist");
  }

  // 회원의 찜 항목 1개 조회
  public Optional<WishItem> findByMemberIdAndProductId(Long memberId, Long productId) {
    String sql = """
        SELECT w.member_id, p.id AS product_id, p.name, p.price, w.quantity, p.imageUrl
        FROM wishlist w
        JOIN product p ON w.product_id = p.id
        WHERE w.member_id = ? AND w.product_id = ?
      """;
    List<WishItem> results = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToWishItem(rs), memberId, productId);
    return results.stream().findFirst();
  }

  // 회원의 전체 찜 목록 조회
  public List<WishItem> findAllByMemberId(Long memberId) {
    String sql = """
        SELECT w.member_id, p.id AS product_id, p.name, p.price, w.quantity, p.imageUrl
        FROM wishlist w
        JOIN product p ON w.product_id = p.id
        WHERE w.member_id = ?
      """;
    return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToWishItem(rs), memberId);
  }

  // 찜 목록에 상품 추가
  public void insert(Long memberId, Long productId) {
    String sql = "INSERT INTO wishlist (member_id, product_id, quantity) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, memberId, productId, 1);  // 최초 수량 1로 설정
  }

  // 찜 상품 수량 조절
  public void updateQuantity(Long memberId, Long productId, int quantity) {
    String sql = "UPDATE wishlist SET quantity = ? WHERE member_id = ? AND product_id = ?";
    jdbcTemplate.update(sql, quantity, memberId, productId);
  }

  // 찜 상품 삭제
  public int delete(Long memberId, Long productId) {
    String sql = "DELETE FROM wishlist WHERE member_id = ? AND product_id = ?";
    return jdbcTemplate.update(sql, memberId, productId);
  }



  private WishItem mapRowToWishItem(ResultSet rs) throws SQLException {
    return new WishItem(
        rs.getLong("member_id"),
        rs.getLong("product_id"),
        rs.getString("name"),
        rs.getInt("price"),
        rs.getInt("quantity"),
        rs.getString("imageUrl")  // ✅ 추가
    );
  }

}
