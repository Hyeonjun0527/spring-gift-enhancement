package gift.product.adapter.persistence.adapter;

import gift.product.adapter.persistence.mapper.ProductEntityMapper;
import gift.product.adapter.persistence.repository.ProductJpaRepository;
import gift.product.domain.model.Product;
import gift.product.domain.port.out.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductPersistenceAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductPersistenceAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable)
                .map(ProductEntityMapper::toDomain);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id)
                .map(ProductEntityMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        var entity = ProductEntityMapper.toEntity(product);
        var savedEntity = productJpaRepository.save(entity);
        return ProductEntityMapper.toDomain(savedEntity);
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
