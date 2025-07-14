package gift.Controller;

import gift.exception.InvalidQuantityException;
import gift.model.Member;
import gift.model.WishItem;
import gift.service.WishlistService;
import gift.util.LoginMember;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/wishlist")
public class WishlistController {

  private final WishlistService wishlistService;

  public WishlistController(WishlistService wishlistService) {
    this.wishlistService = wishlistService;
  }

  // ✅ 전체 찜 목록 조회
  @GetMapping
  public String getWishlist(@LoginMember Member member, Model model) {
    List<WishItem> wishList = wishlistService.getWishList(member.getId());
    model.addAttribute("wishList", wishList);
    return "wishlist/list"; // list.html
  }

  // ✅ 개별 찜 항목 조회
  @GetMapping("/{productId}")
  public String getWishItem(@PathVariable Long productId,
      @LoginMember Member member,
      Model model) {
    WishItem wishItem = wishlistService.getWishItem(member.getId(), productId);
    model.addAttribute("wishItem", wishItem);
    return "wishlist/detail"; // detail.html
  }

  // ✅ 찜 상품 수량 조절
  @PutMapping("/{productId}/quantity")
  public String updateQuantity(@PathVariable Long productId,
                               @RequestParam("quantity") int quantity,
                               @LoginMember Member member,
                               RedirectAttributes redirectAttributes) {

    wishlistService.updateQuantity(member.getId(), productId, quantity);
    redirectAttributes.addFlashAttribute("message", "수량이 변경되었습니다.");

    return "redirect:/api/wishlist";
  }


  // ✅ 찜 상품 삭제
  @DeleteMapping("/{productId}/delete")
  public String deleteWishlistItem(@PathVariable Long productId,
                                   @LoginMember Member member,
                                   RedirectAttributes redirectAttributes) {
    wishlistService.deleteWishListItem(member.getId(), productId);
    redirectAttributes.addFlashAttribute("message", "상품이 찜 목록에서 삭제되었습니다");
    return "redirect:/api/wishlist";
  }
}

