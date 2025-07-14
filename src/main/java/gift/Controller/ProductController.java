package gift.Controller;

import gift.model.Member;
import gift.model.Product;
import gift.service.ProductService;
import gift.service.WishlistService;
import gift.util.LoginMember;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;
  private final WishlistService wishlistService;

  public ProductController(ProductService productService, WishlistService wishlistService) {
    this.productService = productService;
    this.wishlistService = wishlistService;
  }

  // 상품 전체 목록 페이지
  @GetMapping
  public String listProducts(Model model) {
    List<Product> products = productService.findAll();
    model.addAttribute("products", products);
    return "product/list";  // product/list.html 렌더링
  }

  // ✅ 찜하기 처리
  @PostMapping("/{id}/wishlist")
  public String addToWishlist(@PathVariable Long id, @LoginMember Member member) {
    wishlistService.addToWishlist(member.getId(), id);
    return "redirect:/api/products";
  }
}

