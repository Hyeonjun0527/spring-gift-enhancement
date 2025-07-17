package gift.product.adapter.persistence.entity;

import gift.wish.adapter.persistence.entity.WishEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<WishEntity> wishes = new ArrayList<>();

    protected ProductEntity() {
    }

    public ProductEntity(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<WishEntity> getWishes() {
        return wishes;
    }

    public void addWish(WishEntity wish) {
        wishes.add(wish);
        wish.setProduct(this);
    }

    public void removeWish(WishEntity wish) {
        wishes.remove(wish);
        wish.setProduct(null);
    }
} 