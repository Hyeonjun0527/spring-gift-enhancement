package gift.service;

import gift.domain.Wish;
import gift.dto.WishResponse;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public List<WishResponse> getWishes(Long memberId) {
        List<Wish> wishes = wishRepository.findWishByMemberId(memberId);
        return wishes.stream()
                .map(WishResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addWish(Long memberId, Long productId, int quantity) {
        if (wishRepository.exists(memberId, productId)) {
            wishRepository.updateQuantity(memberId, productId,quantity);
        } else {
            wishRepository.save(memberId, productId, quantity);
        }
    }

    @Transactional
    public void updateWish(Long memberId, Long productId, int quantity) {
        if (quantity <= 0) {
            wishRepository.deleteByMemberAndProduct(memberId, productId);
        } else {
            wishRepository.updateQuantity(memberId, productId, quantity);
        }
    }

    public void deleteWish(Long memberId, Long productId) {
        wishRepository.deleteByMemberAndProduct(memberId, productId);
    }
}
