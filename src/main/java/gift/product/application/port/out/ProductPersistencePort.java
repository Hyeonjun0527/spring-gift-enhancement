package gift.product.application.port.out;

import gift.common.pagination.Page;
import gift.common.pagination.Pageable;
import gift.product.domain.model.Product;

import java.util.Optional;

public interface ProductPersistencePort {

    Page<Product> findPage(Pageable pageable);

    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    boolean existsById(Long id);
} 