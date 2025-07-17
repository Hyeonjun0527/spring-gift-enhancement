package gift.wish.adapter.persistence.entity;

import gift.member.adapter.persistence.entity.MemberEntity;
import gift.product.adapter.persistence.entity.ProductEntity;
import jakarta.persistence.*;

import java.util.Objects;

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

    protected WishEntity() {}

    public WishEntity(Long id, MemberEntity member, ProductEntity product, int quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public MemberEntity getMember() { return member; }

    public void setMember(MemberEntity member) {
        if (Objects.nonNull(this.member)) {
            this.member.getWishes().remove(this);
        }
        this.member = member;
        if (Objects.nonNull(member)) {
            member.getWishes().add(this);
        }
    }

    public ProductEntity getProduct() { return product; }

    public void setProduct(ProductEntity product) {
        if (Objects.nonNull(this.product)) {
            this.product.getWishes().remove(this);
        }
        this.product = product;
        if (Objects.nonNull(product)) {
            product.getWishes().add(this);
        }
    }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
} 