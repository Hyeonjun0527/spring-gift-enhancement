package gift.service;

import gift.domain.Product;
import gift.domain.ProductStatus;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllActive() {
        return productRepository.findAllActive();
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public List<Product> getByStatus(ProductStatus status){
        return productRepository.findByStatus(status);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 존재하지 않습니다."));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("해당 상품이 존재하지 않습니다.");
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public void softDelete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("해당 상품이 존재하지 않습니다.");
        }
        productRepository.softDeleteById(id);
    }

    public void update(Long id, Product product) {
        boolean updated = productRepository.updateById(id, product);
        if (!updated) {
            throw new NoSuchElementException("해당 상품이 존재하지 않습니다.");
        }
    }

}

