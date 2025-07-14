package gift.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class Wishlist {

    Long id;

    @NotBlank(message = "멤버 ID는 필수 입력 값입니다.")
    Long memberId;

    @NotBlank(message = "상품 ID는 필수 입력 값입니다.")
    Long productId;

    @NotBlank(message = "수량은 필수 입력 값입니다.")
    @PositiveOrZero(message = "수량은 음수가 될 수 없습니다.")
    Integer quantity;

    public Wishlist() {

    }

    public Wishlist(Long memberId, Long productId, Integer quantity) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Wishlist(Long newId, Long memberId, Long productId, Integer quantity) {
        this.id = newId;
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
