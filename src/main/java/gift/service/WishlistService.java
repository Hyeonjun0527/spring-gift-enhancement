package gift.service;

import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.entity.Wishlist;
import gift.repository.WishlistRepository;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository,
            ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
    }

    public List<ProductResponseDto> getWishlist(Long memberId) {

        List<Wishlist> list = wishlistRepository.findByMemberId(memberId);
        List<Long> productIds = list.stream()
                .map(Wishlist::getProductId)
                .toList();


        List<Product> products = productService.findAllById(productIds);


        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        return list.stream()
                .map(w -> productMap.get(w.getProductId()))
                .filter(Objects::nonNull) // null 방어
                .map(p -> new ProductResponseDto(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getImageUrl()
                ))
                .toList();

    }

    public void addToWishlist(Long memberId, Long productId) {
        wishlistRepository.upsertWishlist(memberId, productId);
    }


    public void removeFromWishlist(Long memberId, Long productId) {
        wishlistRepository.findByMemberIdAndProductId(memberId, productId)
                .ifPresent(w -> wishlistRepository.deleteByMemberIdAndProductId(memberId, productId));
    }
}