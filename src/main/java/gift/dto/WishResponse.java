package gift.dto;

public class WishResponse {

    Long memberId;
    Long productId;
    Integer quantity;

    public WishResponse(Long memberId, Long productId, Integer quantity) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
