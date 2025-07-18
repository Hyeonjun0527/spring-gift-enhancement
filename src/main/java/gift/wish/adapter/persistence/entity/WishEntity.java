package gift.wish.adapter.persistence.entity;

import gift.member.adapter.persistence.entity.MemberEntity;
import gift.product.adapter.persistence.entity.ProductEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "wish")
public class WishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private int quantity;

    protected WishEntity() {
    }

    private WishEntity(Long id, MemberEntity member, ProductEntity product, int quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public static WishEntity create(MemberEntity member,
                                    ProductEntity product,
                                    int quantity) {
        WishEntity wish = new WishEntity(null, null, null, quantity);
        wish.changeMember(member);
        wish.changeProduct(product);
        return wish;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
        
    public void changeMember(MemberEntity newMember) {

        if (this.member != null) {
            this.member.getWishes().remove(this);
        }
        this.member = newMember;

        
    
        if (newMember != null && !newMember.getWishes().contains(this)) {
            newMember.getWishes().add(this);
        }
    }

   public void changeProduct(ProductEntity newProduct) {

        if (this.product != null) {
            this.product.getWishes().remove(this);
        }

        this.product = newProduct;

        if (newProduct != null && !newProduct.getWishes().contains(this)) {
            newProduct.getWishes().add(this);
        }
    }

    public MemberEntity getMember() { return member; }

    public ProductEntity getProduct() { return product; }

} 