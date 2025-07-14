package gift.repository;

import gift.domain.Product;
import gift.domain.ProductStatus;
import gift.domain.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepositoryImpl implements WishRepository  {

    private final JdbcClient jdbcClient;

    public WishRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Long memberId, Long productId, int quantity) {
        jdbcClient.sql("""
            INSERT INTO wishes (member_id, product_id, quantity)
            VALUES (:memberId, :productId, :quantity)
            """)
                .param("memberId", memberId)
                .param("productId", productId)
                .param("quantity", quantity)
                .update();
    }

    @Override
    public void updateQuantity(Long memberId, Long productId, int quantity) {
        jdbcClient.sql("""
            UPDATE wishes
            SET quantity = :quantity
            WHERE member_id = :memberId AND product_id = :productId
            """)
                .param("quantity", quantity)
                .param("memberId", memberId)
                .param("productId", productId)
                .update();

    }

    @Override
    public void deleteByMemberAndProduct(Long memberId, Long productId) {
        jdbcClient.sql("""
            DELETE FROM wishes
            WHERE member_id = :memberId AND product_id = :productId
            """)
                .param("memberId", memberId)
                .param("productId", productId)
                .update();

    }

    @Override
    public List<Wish> findWishByMemberId(Long memberId) {
        return jdbcClient.sql("""
        SELECT 
            w.id, 
            w.member_id, 
            w.quantity,
            p.id AS product_id,
            p.name AS product_name,
            p.price AS product_price,
            p.image_url AS product_image_url,
            p.status AS product_status
        FROM wishes w
        JOIN products p ON w.product_id = p.id
        WHERE w.member_id = :memberId
        """)
                .param("memberId", memberId)
                .query((rs, rowNum) -> {
                    Product product = new Product(
                            rs.getString("product_name"),
                            rs.getInt("product_price"),
                            rs.getString("product_image_url"),
                            ProductStatus.valueOf(rs.getString("product_status"))
                    );

                    return new Wish(
                            rs.getLong("id"),
                            rs.getLong("member_id"),
                            rs.getLong("product_id"),
                            rs.getInt("quantity"),
                            product
                    );
                })
                .list();
    }




    @Override
    public boolean exists(Long memberId, Long productId) {
        Integer count = jdbcClient.sql("""
            SELECT COUNT(*) FROM wishes
            WHERE member_id = :memberId AND product_id = :productId
            """)
                .param("memberId", memberId)
                .param("productId", productId)
                .query(Integer.class)
                .single();

        return count != null && count > 0;
    }
}
