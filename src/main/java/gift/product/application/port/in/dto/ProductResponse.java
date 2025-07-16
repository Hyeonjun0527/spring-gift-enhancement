package gift.product.application.port.in.dto;

public record ProductResponse(
        Long id,
        String name,
        int price,
        String imageUrl
) {
    public static ProductResponse of(Long id, String name, int price, String imageUrl) {
        return new ProductResponse(id, name, price, imageUrl);
    }
} 