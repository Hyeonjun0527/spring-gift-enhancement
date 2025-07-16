package gift.product.application.usecase;

import gift.product.application.port.in.ProductUseCase;
import gift.product.application.port.in.dto.CreateProductRequest;
import gift.product.application.port.in.dto.UpdateProductRequest;
import gift.product.application.port.out.ProductPersistencePort;
import gift.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductInteractor implements ProductUseCase {
    private final ProductPersistencePort productPersistencePort;

    public ProductInteractor(ProductPersistencePort productPersistencePort) {
        this.productPersistencePort = productPersistencePort;
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productPersistencePort.findAll(pageable);
    }

    @Override
    public Product getProduct(Long id) {
        return productPersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + id));
    }

    @Override
    public Product addProduct(CreateProductRequest request) {
        Product product = Product.of(null, request.name(), request.price(), request.imageUrl());
        return productPersistencePort.save(product);
    }

    @Override
    public void updateProduct(Long id, UpdateProductRequest request) {
        Product existingProduct = productPersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + id));

        Product updatedProduct = Product.of(
                id,
                request.name() != null ? request.name() : existingProduct.getName(),
                request.price() != null ? request.price() : existingProduct.getPrice(),
                request.imageUrl() != null ? request.imageUrl() : existingProduct.getImageUrl()
        );
        productPersistencePort.save(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productPersistencePort.existsById(id)) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + id);
        }
        productPersistencePort.deleteById(id);
    }
} 