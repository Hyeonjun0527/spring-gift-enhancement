package gift.product.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Product {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private List<Option> options;

    private Product(Long id, String name, int price, String imageUrl, List<Option> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
    }

    public static Product create(Long id, String name, int price, String imageUrl, List<Option> options) {

        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("상품에는 하나 이상의 옵션이 있어야 합니다.");
        }

        Set<String> distinctOptionNames = options.stream()
                .map(Option::getName)
                .collect(Collectors.toSet());

        if (distinctOptionNames.size() < options.size()) {
            throw new IllegalArgumentException("옵션 이름은 중복될 수 없습니다.");
        }

        return new Product(id, name, price, imageUrl, new ArrayList<>(options));
    }

    public static Product of(Long id, String name, int price, String imageUrl, List<Option> options) {
        return new Product(id, name, price, imageUrl, options);
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

    public List<Option> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}