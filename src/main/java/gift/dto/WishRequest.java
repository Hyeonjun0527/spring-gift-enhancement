package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class WishRequest {

    @NotNull(message = "상품 ID는 필수 입력 값입니다.")
    Long productId;

    @NotNull(message = "수량은 필수 입력 값입니다.")
    @PositiveOrZero(message = "수량은 음수가 될 수 없습니다.")
    Integer quantity;

    public WishRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
