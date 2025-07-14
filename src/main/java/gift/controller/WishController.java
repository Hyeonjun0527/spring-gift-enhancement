package gift.controller;

import gift.domain.Wish;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.service.WishService;
import gift.domain.Member;
import gift.resolver.LoginMember;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public List<WishResponse> getWishes(@LoginMember Member member) {
        return wishService.getWishes(member.getId());
    }

    @PostMapping
    public void addWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishService.addWish(member.getId(), request.getProductId(), request.getQuantity());
    }

    @PatchMapping
    public void updateWish(
            @RequestBody WishRequest request,
            @LoginMember Member member
    ) {
        wishService.updateWish(member.getId(), request.getProductId(), request.getQuantity());
    }

    @DeleteMapping
    public void deleteWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishService.deleteWish(member.getId(), request.getProductId());
    }


}
