package gift.service;

import gift.dto.LoginMemberDto;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.model.Wishlist;
import gift.repository.WishlistRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final ProductService productService;
    private final MemberService memberService;
    private final WishlistRepository wishlistRepository;

    public WishlistService(ProductService productService, MemberService memberService,
        WishlistRepository wishlistRepository) {
        this.productService = productService;
        this.memberService = memberService;
        this.wishlistRepository = wishlistRepository;
    }

    public WishResponse create(WishRequest request, LoginMemberDto memberDto) {
        Long memberId = memberService.findByEmail(memberDto.getEmail()).getId();
        Long productId = productService.getProduct(request.getProductId()).getId();

        Optional<Wishlist> existingWish = wishlistRepository.findByMemberIdAndProductId(memberId,
            productId);
        if (existingWish.isPresent()) {
            throw new IllegalStateException("해당 상품은 이미 위시리스트에 존재합니다.");
        }

        Wishlist wishlist = new Wishlist(memberId, productId, request.getQuantity());
        Wishlist saved = wishlistRepository.save(wishlist);

        return new WishResponse(saved.getMemberId(), saved.getProductId(), saved.getQuantity());
    }

    public List<WishResponse> findAllByMemberId(LoginMemberDto memberDto) {
        Long memberId = memberService.findByEmail(memberDto.getEmail()).getId();
        List<Wishlist> wishlists = wishlistRepository.findAll(memberId);

        return wishlists.stream()
            .map(w -> new WishResponse(w.getMemberId(), w.getProductId(), w.getQuantity()))
            .collect(Collectors.toList());
    }

    public void deleteWishlist(LoginMemberDto memberDto, Long productId) {
        Long memberId = memberService.findByEmail(memberDto.getEmail()).getId();
        if (wishlistRepository.findByMemberIdAndProductId(memberId, productId).isEmpty()) {
            throw new IllegalArgumentException("삭제할 위시리스트가 존재하지 않습니다.");
        }
        wishlistRepository.delete(memberId, productId);
    }
}
