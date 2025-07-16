package gift.product.application.port.in.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static gift.common.validation.ValidationMessages.*;

public record UpdateProductRequest(
        @Size(max = 15, message = NAME_SIZE_MESSAGE)
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s()\\[\\]+&/_-]*$", message = NAME_PATTERN_MESSAGE)
        @Pattern(regexp = "^(?!.*카카오).*$", message = NAME_KAKAO_MESSAGE)
        String name,

        @Min(value = 1, message = PRICE_MIN_MESSAGE)
        Integer price,

        String imageUrl
) {
        public static UpdateProductRequest of(String name, int price, String imageUrl) {
                return new UpdateProductRequest(name, price, imageUrl);
        }
}
