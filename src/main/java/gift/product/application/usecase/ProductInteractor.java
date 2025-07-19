package gift.product.application.usecase;

import gift.product.application.port.in.ProductUseCase;
import gift.product.application.port.in.dto.CreateProductRequest;
import gift.product.application.port.in.dto.UpdateProductRequest;
import gift.product.domain.model.Product;
import gift.product.domain.port.out.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductInteractor implements ProductUseCase {
    private final ProductRepository productRepository;

    public ProductInteractor(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + id));
    }

    @Override
    public Product addProduct(CreateProductRequest request) {
        Product product = Product.of(null, request.name(), request.price(), request.imageUrl());
        return productRepository.save(product);
    }

    @Override
    public void updateProduct(Long id, UpdateProductRequest request) {

        if (request == null) throw new IllegalArgumentException("수정 명렁 정보가 없습니다.");

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + id));

        String name = existingProduct.getName();
        int price = existingProduct.getPrice();
        String imageUrl = existingProduct.getImageUrl();

        if (request.name() != null) {
            name = request.name();
        }
        if (request.price() != null) {
            price = request.price();
        }

        if (request.imageUrl() != null) {
            imageUrl = request.imageUrl();
        }

        Product updatedProduct = Product.of(id, name, price, imageUrl);

        productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + id);
        }
        productRepository.deleteById(id);
    }
} 