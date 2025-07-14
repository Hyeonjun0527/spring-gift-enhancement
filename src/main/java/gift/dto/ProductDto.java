package gift.dto;

import gift.model.Product;
import jakarta.validation.constraints.*;

public class ProductDto {

  private Long id;

  @NotBlank(message = "상품 이름은 필수입니다.")
  @Size(max=15, message = "상품 이름은 최대 15자까지 입력가능 합니다")
  @Pattern(
      regexp = "^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-&/_]*$",
      message = "상품 이름에는 (), [], +, -, &, /, _ 외의 특수문자는 사용 불가합니다"
  )
  private String name;

  @PositiveOrZero(message = "가격은 0 이상이어야 합니다")
  private int price;
  private String imageUrl;

  public ProductDto() {
  }

  public ProductDto(Long id, String name, int price, String imageUrl) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  // 도메인 객체를 DTO로 변환 (null 방지 포함)
  public static ProductDto from(Product product) {
    if (product == null) {
      return null;  // 또는 throw new IllegalArgumentException("null product");
    }
    return new ProductDto(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getImageUrl()
    );
  }

  // DTO를 도메인 객체로 변환
  public Product toEntity() {
    return new Product(id, name, price, imageUrl);
  }

  // getter & setter
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
