package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    private Product toEntity(ProductRequestDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getImageUrl());
    }

    private ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl());
    }

    public List<ProductResponseDto> findAllProduct() {
        return productRepository.findAll().stream()
                .map(p -> new ProductResponseDto(p.getId(), p.getName(), p.getPrice(),
                        p.getImageUrl()))
                .toList();
    }



    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {

        validateNameContent(productRequestDto.getName());
        Product product = toEntity(productRequestDto);
        Product savedProduct = productRepository.save(product);
        return toDto(savedProduct);
    }


    public ProductResponseDto findProduct(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new NoSuchElementException("product does not exist.");
        }
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl());
    }


    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {

        validateNameContent(productRequestDto.getName());
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new NoSuchElementException("product does not exist.");
        }
        //db저장
        product.updateProduct(productRequestDto.getName(), productRequestDto.getPrice(),
                productRequestDto.getImageUrl());
        Product updateProduct = productRepository.update(id, product);

        return new ProductResponseDto(updateProduct.getId(), updateProduct.getName(),
                updateProduct.getPrice(), updateProduct.getImageUrl());
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new NoSuchElementException("Product does not exist.");
        }
        productRepository.deleteById(id);
    }

    private void validateNameContent(String name) {
        if (name.contains("카카오")) {
            throw new IllegalArgumentException("상품 이름은 '카카오'를 포함할 수 없습니다.");
        }
    }
    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids);
    }
}
