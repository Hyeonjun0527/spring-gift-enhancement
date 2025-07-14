package gift.model;

import gift.validation.ValidProductName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class Product {

    private Long id;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(max = 15, message = "이름은 15자까지 입력 가능합니다.")
    @ValidProductName
    private String name;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @PositiveOrZero(message = "가격은 음수가 될 수 없습니다.")
    private Integer price;

    @NotBlank(message = "이미지 Url은 필수 입력 값입니다.")
    @Pattern(regexp = "^https?:\\/\\/.*\\.(jpg|jpeg|png|gif|bmp|tiff)$", message = "imageUrl의 형식이 잘못되었습니다.")
    private String imageUrl;

    private boolean needsMdApproval;

    // 생성자
    public Product() {
    }

    public Product(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getter & Setter
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getNeedsMdApproval() {
        return needsMdApproval;
    }

    public void setNeedsMdApproval(boolean needsMdApproval) {
        this.needsMdApproval = needsMdApproval;
    }

}
