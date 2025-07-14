package gift.domain;

public class Product {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private ProductStatus status;
    private boolean isDeleted;

    public Product() {
    }

    public Product(Long id, String name, int price, String imageUrl, ProductStatus status, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Product(String name, int price, String imageUrl, ProductStatus status) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public Product(String name, int price, String imageUrl, ProductStatus status, boolean isDeleted) {
        this(null, name, price, imageUrl,status, false);
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

    public ProductStatus getStatus() {
        return status;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setId(Long id){
        this.id = id;
    }

}