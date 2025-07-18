package gift.product.adapter.persistence.mapper;

import gift.product.adapter.persistence.entity.ProductEntity;
import gift.product.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEntityMapperTest {
    @Test
    @DisplayName("ProductEntity -> Product 변환")
    void toDomain() {
        ProductEntity entity = ProductEntity.of( "A", 100, "a.jpg");
        Product domain = ProductEntityMapper.toDomain(entity);
        assertThat(domain.getName()).isEqualTo("A");
        assertThat(domain.getPrice()).isEqualTo(100);
        assertThat(domain.getImageUrl()).isEqualTo("a.jpg");
    }

    @Test
    @DisplayName("Product -> ProductEntity 변환")
    void toEntity() {
        Product domain = Product.of(2L, "B", 200, "b.jpg");
        ProductEntity entity = ProductEntityMapper.toEntity(domain);
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getName()).isEqualTo("B");
        assertThat(entity.getPrice()).isEqualTo(200);
        assertThat(entity.getImageUrl()).isEqualTo("b.jpg");
    }
} 