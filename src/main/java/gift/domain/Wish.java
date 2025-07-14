package gift.domain;
import gift.domain.Product;

public class Wish {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final int quantity;
    private Product product;

    public Wish(Long id, Long memberId, Long productId, int quantity, Product product) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
    }

    public Wish(Long memberId, Long productId, int quantity) {
        this(null, memberId, productId, quantity, null);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity(){
        return quantity;
    }

    public Product getProduct(){
        return product;
    }
}