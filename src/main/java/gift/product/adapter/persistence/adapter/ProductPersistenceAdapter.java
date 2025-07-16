package gift.product.adapter.persistence.adapter;
import gift.product.adapter.persistence.mapper.ProductPersistenceMapper;
import gift.product.adapter.persistence.repository.ProductJpaRepository;
import gift.product.application.port.out.ProductPersistencePort;
import gift.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {

    private final ProductJpaRepository productJpaRepository;

    public ProductPersistenceAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable)
                .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id)
                .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        var entity = ProductPersistenceMapper.toEntity(product);
        var savedEntity = productJpaRepository.save(entity);
        return ProductPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return productJpaRepository.existsById(id);
    }
}
