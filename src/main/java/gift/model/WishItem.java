package gift.model;

public class WishItem {

  private Long memberId;
  private Long productId;
  private String productName;
  private int price;
  private int quantity;
  private String imageUrl;

  public WishItem(Long memberId, Long productId, String productName, int price, int quantity, String imageUrl) {
    this.memberId = memberId;
    this.productId = productId;
    this.productName = productName;
    this.price = price;
    this.quantity = quantity;
    this.imageUrl = imageUrl;
  }

  public Long getMemberId() {
    return memberId;
  }
  public Long getProductId() {
    return productId;
  }
  public String getProductName() {
    return productName;
  }
  public int getPrice() {
    return price;
  }
  public int getQuantity() {
    return quantity;
  }
  public int getTotalPrice() {
    return price * quantity;
  }
  public String getImageUrl() {
    return imageUrl;
  }

  //수량 조절
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
