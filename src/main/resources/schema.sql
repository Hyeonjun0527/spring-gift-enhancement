  CREATE TABLE product(
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(255) NOT NULL,
      price INT NOT NULL,
      imageUrl VARCHAR(1024)
  );

  CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
  );

CREATE TABLE wishlist(
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (member_id, product_id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (member_id) REFERENCES member(id)
);