package gift.repository;

import gift.model.Product;
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
public class ProductRepository {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    public ProductRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("product")
            .usingGeneratedKeyColumns("id");
    }

    // 상품 저장
    public Product save(Product product) {
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("name", product.getName())
            .addValue("price", product.getPrice())
            .addValue("imageUrl", product.getImageUrl())
            .addValue("needsMdApproval", product.getNeedsMdApproval());
        Long newId = jdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Product(newId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    // 상품 전체 조회
    public List<Product> findAll() {
        String sql = "SELECT id, name, price, imageUrl FROM PRODUCT WHERE needsMdApproval = false";
        return jdbcTemplate.query(sql, productRowMapper());
    }

    // 상품 단건 조회
    public Optional<Product> findById(Long id) {
        String sql = "SELECT id, name, price, imageUrl FROM PRODUCT WHERE id = ? and needsMdApproval = false";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, productRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // 상품 수정
    public void update(Product product) {
        String sql = "UPDATE PRODUCT SET name = ?, price = ?, imageUrl = ?, needsMdApproval = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(),
            product.getNeedsMdApproval(), product.getId());
    }

    // 상품 삭제
    public void delete(Long id) {
        String sql = "DELETE FROM PRODUCT WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // DB 초기화
    public void deleteAll() {
        String sql = "DELETE FROM PRODUCT";
        jdbcTemplate.update(sql);
    }

    private RowMapper<Product> productRowMapper() {
        return ((rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("imageUrl");
            return new Product(id, name, price, imageUrl);
        });
    }

}
