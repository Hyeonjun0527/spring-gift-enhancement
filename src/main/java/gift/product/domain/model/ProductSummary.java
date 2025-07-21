package gift.product.domain.model;

public class ProductSummary {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    private ProductSummary(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductSummary of(Long id, String name, int price, String imageUrl) {
        return new ProductSummary(id, name, price, imageUrl);
    }
}
