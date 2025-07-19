package gift.product.adapter.web.mapper;
import gift.product.application.port.in.dto.ProductResponse;
import gift.product.domain.model.Product;

public class ProductWebMapper {

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
} 