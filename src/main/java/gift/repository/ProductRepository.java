package gift.repository;

import gift.domain.Product;
import gift.domain.ProductStatus;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    List<Product> findAllActive();
    List<Product> findAll();
    List<Product> findByStatus(ProductStatus status);
    Optional<Product> findById(Long id);
    void deleteById(Long id);
    void softDeleteById(Long id);
    boolean existsById(Long id);
    boolean updateById(Long id, Product product);
}

