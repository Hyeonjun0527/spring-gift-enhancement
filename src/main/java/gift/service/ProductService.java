package gift.service;

import gift.model.Product;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 상품 저장
    public Product addProduct(Product product) {
        // 카카오가 포함된 이름은 MD의 승인 필요
        boolean isContainedKakao = product.getName().contains("카카오");
        product.setNeedsMdApproval(isContainedKakao);

        return productRepository.save(product);
    }

    // 상품 전체 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 상품 단건 조회
    public Product getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new IllegalArgumentException(
                "id: " + id + ". 해당 ID의 상품이 존재하지 않습니다.");
        }
        return product.get();
    }

    // 상품 수정
    public void updateProduct(Long id, Product product) {
        // 수정 전에 존재 여부 체크
        if (productRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("id: " + id + ". 수정할 상품이 존재하지 않습니다.");
        }
        // 카카오가 포함된 이름은 MD의 승인 필요
        boolean isContainedKakao = product.getName().contains("카카오");
        product.setNeedsMdApproval(isContainedKakao);

        productRepository.update(product);
    }

    // 상품 삭제
    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("삭제할 상품이 존재하지 않습니다.");
        }
        productRepository.delete(id);
    }
}
