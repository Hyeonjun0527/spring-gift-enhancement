package gift.entity;

public class Wishlist {
    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;

    public Wishlist() {}

    public Wishlist(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = 1;
    }
    public Wishlist(Long id, Long memberId, Long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
