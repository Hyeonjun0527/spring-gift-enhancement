package gift.dto;

import gift.domain.ProductStatus;
import gift.domain.Wish;

public class WishResponse {
    private final Long id;
    private final Long productId;
    private final int quantity;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final ProductStatus status;

    public WishResponse(Long id, Long productId, int quantity, String name, int price, String imageUrl, ProductStatus status) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public static WishResponse from(Wish wish) {
        return new WishResponse(
                wish.getId(),
                wish.getProductId(),
                wish.getQuantity(),
                wish.getProduct().getName(),
                wish.getProduct().getPrice(),
                wish.getProduct().getImageUrl(),
                wish.getProduct().getStatus()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
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

    public ProductStatus getStatus() {
        return status;
    }
}



