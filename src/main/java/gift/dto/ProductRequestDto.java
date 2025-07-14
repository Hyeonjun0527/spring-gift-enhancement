package gift.dto;

import jakarta.validation.constraints.*;


public class ProductRequestDto {

    @NotBlank(message = "상품 이름은 필수 입력 사항입니다.")
    @Size(max = 15, message = "상품 이름은 공백 포함 최대 15자까지 가능합니다.")
    @Pattern(
            regexp = "^(?=.*\\p{L})[\\p{L}\\p{N} ()\\[\\]+\\-\\&/_]{1,15}$",
            message = "상품명은 반드시 최소 한 글자 이상의 문자를 포함해야 하며, 허용되지 않은 특수문자는 사용할 수 없습니다."
    )
    private String name;


    @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
    private String imageUrl;

    public ProductRequestDto() {
    }



    public ProductRequestDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
