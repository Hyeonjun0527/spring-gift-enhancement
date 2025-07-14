package gift.controller;

import gift.dto.WishlistProductDto;
import gift.dto.WishlistRequestDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import gift.security.LoginMember;
import gift.service.WishlistService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final WishlistService wishlistService;

    public WishlistController(ProductRepository productRepository,
            WishlistRepository wishlistRepository, WishlistService wishlistService) {
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.wishlistService = wishlistService;
    }



    //위시리스트의 목록을 가져오기
    @GetMapping
    public List<WishlistProductDto> getWishlist(@LoginMember Member member) {
        return wishlistRepository.findByMemberId(member.getId()).stream()
                .map(w -> {//dto로 가공
                    Product product = productRepository.findById(w.getProductId());
                    return new WishlistProductDto(product.getName(), w.getQuantity());
                })
                .collect(Collectors.toList());
    }


    @PostMapping
    public ResponseEntity<Void> addToWishlist(@LoginMember Member member,
            @RequestBody WishlistRequestDto request) {
        wishlistService.addToWishlist(member.getId(), request.productId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@LoginMember Member member,
            @PathVariable Long productId) {
        wishlistService.removeFromWishlist(member.getId(), productId);
        return ResponseEntity.noContent().build();
    }


}
