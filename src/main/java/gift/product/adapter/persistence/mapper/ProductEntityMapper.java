package gift.product.adapter.persistence.mapper;

import gift.product.adapter.persistence.entity.ProductEntity;
import gift.product.domain.model.Product;

public class ProductEntityMapper {

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return Product.of(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageUrl()
        );
    }

    public static ProductEntity toEntity(Product domain) {
        if (domain == null) {
            return null;
        }
        return ProductEntity.of(
                domain.getId(),
                domain.getName(),
                domain.getPrice(),
                domain.getImageUrl()
        );
    }
} 