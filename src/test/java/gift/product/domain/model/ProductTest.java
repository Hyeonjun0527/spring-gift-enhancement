package gift.product.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {
    @Test
    @DisplayName("도메인테스트")
    void createAndGetters() {
        Long id = 1L;
        String name = "테스트상품";
        int price = 1000;
        String imageUrl = "img.jpg";

        Product product = Product.of(id, name, price, imageUrl);

        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getImageUrl()).isEqualTo(imageUrl);
    }
} 