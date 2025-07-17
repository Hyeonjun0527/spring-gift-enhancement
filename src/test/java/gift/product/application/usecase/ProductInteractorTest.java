package gift.product.application.usecase;

import gift.product.application.port.in.dto.CreateProductRequest;
import gift.product.application.port.in.dto.UpdateProductRequest;
import gift.product.domain.model.Product;
import gift.product.domain.port.out.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class ProductInteractorTest {
    private ProductRepository productRepository;
    private ProductInteractor productInteractor;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productInteractor = new ProductInteractor(productRepository);
    }

    @Test
    @DisplayName("상품 페이지 조회")
    void getProducts() {
        Product p1 = Product.of(1L, "A", 100, "a.jpg");
        Product p2 = Product.of(2L, "B", 200, "b.jpg");
        Page<Product> page = new PageImpl<>(List.of(p1, p2), PageRequest.of(0, 10), 2);
        given(productRepository.findAll(any(Pageable.class))).willReturn(page);

        Page<Product> result = productInteractor.getProducts(PageRequest.of(0, 10));
        assertThat(result.getContent()).containsExactly(p1, p2);
    }

    @Test
    @DisplayName("ID로 상품 조회 - 성공")
    void getProduct_success() {
        Product p = Product.of(1L, "A", 100, "a.jpg");
        given(productRepository.findById(1L)).willReturn(Optional.of(p));
        Product result = productInteractor.getProduct(1L);
        assertThat(result).isEqualTo(p);
    }

    @Test
    @DisplayName("ID로 상품 조회 - 실패시 예외")
    void getProduct_fail() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> productInteractor.getProduct(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("상품 추가")
    void addProduct() {
        CreateProductRequest req = new CreateProductRequest("A", 100, "a.jpg");
        Product saved = Product.of(1L, "A", 100, "a.jpg");
        given(productRepository.save(any(Product.class))).willReturn(saved);
        Product result = productInteractor.addProduct(req);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("상품 수정 - 존재하지 않으면 예외")
    void updateProduct_fail() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        UpdateProductRequest req = new UpdateProductRequest("B", 200, "b.jpg");
        assertThatThrownBy(() -> productInteractor.updateProduct(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("상품 수정 - 일부 필드만 변경")
    void updateProduct_partial() {
        Product existing = Product.of(1L, "A", 100, "a.jpg");
        given(productRepository.findById(1L)).willReturn(Optional.of(existing));
        UpdateProductRequest req = new UpdateProductRequest(null, 200, null);
        productInteractor.updateProduct(1L, req);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product updated = captor.getValue();
        assertThat(updated.getName()).isEqualTo("A");
        assertThat(updated.getPrice()).isEqualTo(200);
        assertThat(updated.getImageUrl()).isEqualTo("a.jpg");
    }

    @Test
    @DisplayName("상품 삭제 - 존재하지 않으면 예외")
    void deleteProduct_fail() {
        given(productRepository.existsById(1L)).willReturn(false);
        assertThatThrownBy(() -> productInteractor.deleteProduct(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("상품 삭제 - 정상 동작")
    void deleteProduct_success() {
        given(productRepository.existsById(1L)).willReturn(true);
        productInteractor.deleteProduct(1L);
        verify(productRepository).deleteById(1L);
    }
} 