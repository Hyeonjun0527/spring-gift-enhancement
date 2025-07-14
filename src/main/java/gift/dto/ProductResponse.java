package gift.dto;

import gift.domain.Product;
import gift.domain.ProductStatus;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final ProductStatus status;
    private final boolean isDeleted;

    public ProductResponse(Long id, String name, int price, String imageUrl, ProductStatus status, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStatus(),
                product.isDeleted()
        );
    }
}
