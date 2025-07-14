package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.dto.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@DisplayName("Product 삽입 Test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

  @LocalServerPort
  private int port = 8080;

  private RestClient client = RestClient.builder().build();

  @Test
  @DisplayName("[Product.name] 최대 글자 수 길이 제한 초과")
  void overLength_ProductName() {
    // given: 유효하지 않은 상품명
    ProductDto productDto = new ProductDto();
    productDto.setName("이름이15자를초과하는상품명입니다");
    productDto.setPrice(1000);
    productDto.setImageUrl("https://example.com/image.jpg");

    String url = "http://localhost:" + port + "/admin/products";

    // when & then
    HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
      client.post()
          .uri(url)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body("name=" + productDto.getName() +
              "&price=" + productDto.getPrice() +
              "&imageUrl=" + productDto.getImageUrl())
          .retrieve()
          .toBodilessEntity();
    });

    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }


  @Test
  @DisplayName("[Product.name] 허용되지 않은 특수문자 사용")
  void invalidHyperText_ProductName() {
    // given: 유효하지 않은 상품명
    ProductDto productDto = new ProductDto();
    productDto.setName("**아메리카노");
    productDto.setPrice(1000);
    productDto.setImageUrl("https://example.com/image.jpg");

    String url = "http://localhost:" + port + "/admin/products";

    // when & then
    HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
      client.post()
          .uri(url)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body("name=" + productDto.getName() +
              "&price=" + productDto.getPrice() +
              "&imageUrl=" + productDto.getImageUrl())
          .retrieve()
          .toBodilessEntity();
    });
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("[Product.name] '카카오'가 상품명에 포함")
  void invalidWord_ProductName() {
    // given: 유효하지 않은 상품명
    ProductDto productDto = new ProductDto();
    productDto.setName("카카오 나무");
    productDto.setPrice(1000);
    productDto.setImageUrl("https://example.com/image.jpg");

    String url = "http://localhost:" + port + "/admin/products";

    // when & then
    HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
      client.post()
          .uri(url)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body("name=" + productDto.getName() +
              "&price=" + productDto.getPrice() +
              "&imageUrl=" + productDto.getImageUrl())
          .retrieve()
          .toBodilessEntity();
    });
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("[Product.price] 음수 가격 삽입")
  void minusPrice_ProductPrice() {
    // given: 유효하지 않은 상품명
    ProductDto productDto = new ProductDto();
    productDto.setName("주사위");
    productDto.setPrice(-7200);
    productDto.setImageUrl("https://example.com/image.jpg");

    String url = "http://localhost:" + port + "/admin/products";

    // when & then
    HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
      client.post()
          .uri(url)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body("name=" + productDto.getName() +
              "&price=" + productDto.getPrice() +
              "&imageUrl=" + productDto.getImageUrl())
          .retrieve()
          .toBodilessEntity();
    });
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
