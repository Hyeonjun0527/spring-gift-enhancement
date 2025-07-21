package gift.product.application.usecase;

import gift.product.application.port.in.ProductUseCase;
import gift.product.application.port.in.dto.AdminUpdateProductRequest;
import gift.product.application.port.in.dto.CreateProductRequest;
import gift.product.application.port.in.dto.UpdateProductRequest;
import gift.product.domain.model.Option;
import gift.product.domain.model.Product;
import gift.product.domain.port.out.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductInteractor implements ProductUseCase {
    private final ProductRepository productRepository;

    public ProductInteractor(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + id));
    }

    @Override
    public Product addProduct(CreateProductRequest request) {
        List<Option> options = request.optionRequests()
                .stream()
                .map(req -> Option.create(null,null,req.name(),req.quantity()))
                .toList();
        Product product = Product.create(null, request.name(), request.price(), request.imageUrl(),options);
        return productRepository.save(product);
    }

    @Override
    public void updateProduct(Long id, UpdateProductRequest request) {

        if (request == null) throw new IllegalArgumentException("수정 명렁 정보가 없습니다.");

        updateProductInternal(id, request.name(), request.price(), request.imageUrl());
    }

    @Override
    public void updateProductForAdmin(Long id, AdminUpdateProductRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("상품 ID가 없습니다.");
        }
        if (request == null) {
            throw new IllegalArgumentException("업데이트 요청 정보가 없습니다.");
        }
        updateProductInternal(id, request.name(), request.price(), request.imageUrl());
    }

    private void updateProductInternal(Long id, String name, Integer price, String imageUrl) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + id));

        Product updatedProduct = Product.of(
                id,
                name != null ? name : existingProduct.getName(),
                price != null ? price : existingProduct.getPrice(),
                imageUrl != null ? imageUrl : existingProduct.getImageUrl(),
                existingProduct.getOptions()
        );
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