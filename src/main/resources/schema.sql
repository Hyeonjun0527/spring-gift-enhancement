CREATE TABLE product
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    price           INT          NOT NULL,
    imageUrl        VARCHAR(255) NOT NULL,
    needsMdApproval BOOLEAN      NOT NULL
);

CREATE TABLE member
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE wishlist
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    memberId  BIGINT NOT NULL,
    productId BIGINT NOT NULL,
    quantity  INT    NOT NULL,

    CONSTRAINT fk_wishlist_member FOREIGN KEY (memberId) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_product FOREIGN KEY (productId) REFERENCES product (id) ON DELETE CASCADE,
    CONSTRAINT uc_member_product UNIQUE (memberId, productId)
);

-- 테스트용 product
INSERT into product(name, price, imageUrl, needsMdApproval)
values ('coffee', 2000, 'http://coffee.jpg', false);

INSERT into product(name, price, imageUrl, needsMdApproval)
values ('latte', 3000, 'http://latte.jpg', false);

INSERT into product(name, price, imageUrl, needsMdApproval)
values ('tea', 2500, 'http://icetea.jpg', false);
