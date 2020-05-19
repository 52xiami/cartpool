DROP DATABASE IF EXISTS cartpool;
CREATE DATABASE cartpool;
USE cartpool;

CREATE TABLE cart_pool (
    id INTEGER NOT NULL AUTO_INCREMENT,
    pool_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    neighborhood VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    zip VARCHAR(5) NOT NULL,
    leader INTEGER NOT NULL,
    creation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE user (
    id INTEGER NOT NULL AUTO_INCREMENT,
    email VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(500) NOT NULL,
    screen_name VARCHAR(50) UNIQUE NOT NULL,
    nickname VARCHAR(50) UNIQUE NOT NULL,
    pool_id INTEGER,
    pool_status VARCHAR(50) DEFAULT 'INIT',
    contribution INTEGER DEFAULT 0,
	role VARCHAR(20) NOT NULL,
	active BOOLEAN DEFAULT FALSE,
    token VARCHAR(32) UNIQUE,
    creation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (pool_id)
        REFERENCES cart_pool (id)
            ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE store (
	id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    street VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(50),
    zip VARCHAR(5),
    PRIMARY KEY (id)
);

CREATE TABLE product (
	id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    image_url VARCHAR(255),
    brand VARCHAR(50),
    category VARCHAR(50),
    unit VARCHAR(50),
    price DOUBLE PRECISION,
    PRIMARY KEY (id)
);

CREATE TABLE inventory (
    store_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (store_id, product_id),
    FOREIGN KEY (store_id)
        REFERENCES store (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id)
        REFERENCES product (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE user_order (
    id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    pool_id INTEGER,
    store_id INTEGER,
    amount DOUBLE PRECISION NOT NULL,
    status VARCHAR(10) NOT NULL,
    pickup_method VARCHAR(10) NOT NULL,
    pickup_user INTEGER,
    street VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip VARCHAR(5) NOT NULL,
    creation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    checkout_time DATETIME,
    due_time DATETIME,
    pickup_time DATETIME,
    delivered_time DATETIME,
    update_time DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES user (id)
            ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (pool_id)
        REFERENCES cart_pool (id)
            ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (store_id)
        REFERENCES store (id)
            ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE order_details (
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    tax_fee DOUBLE PRECISION,
    price DOUBLE PRECISION,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id)
        REFERENCES user_order (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
);



INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('milk', 'milk desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'acd', 'd', 'L', '9.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('egg', 'egg desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'ddd', 'd', 'L', '12.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('tea', 'tea desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'aaa', 'm', 'L', '5.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('milk2', 'milk desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'acd', 'd', 'L', '9.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('egg2', 'egg desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'ddd', 'd', 'L', '12.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('tea2', 'tea desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'aaa', 'm', 'L', '5.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('milk3', 'milk desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'acd', 'd', 'L', '9.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('egg3', 'egg desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'ddd', 'd', 'L', '12.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('tea3', 'tea desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'aaa', 'm', 'L', '5.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('milk4', 'milk desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'acd', 'd', 'L', '9.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('egg4', 'egg desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'ddd', 'd', 'L', '12.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('tea4', 'tea desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'aaa', 'm', 'L', '5.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('milk5', 'milk desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'acd', 'd', 'L', '9.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('egg5', 'egg desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'ddd', 'd', 'L', '12.99');
INSERT INTO `cartpool`.`product` (`name`, `description`, `image_url`, `brand`, `category`, `unit`, `price`) VALUES ('tea5', 'tea desc', 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png', 'aaa', 'm', 'L', '5.99');

INSERT INTO `cartpool`.`store` (`name`, `street`, `city`, `state`, `zip`) VALUES ('STORE1', '123 street', 'city1', 'CA', '99999');
INSERT INTO `cartpool`.`store` (`name`, `street`, `city`, `state`, `zip`) VALUES ('STORE2', '567 ave', 'city2', 'CA', '88888');
INSERT INTO `cartpool`.`store` (`name`, `street`, `city`, `state`, `zip`) VALUES ('STORE3', '333 street', 'city1', 'CA', '77777');

INSERT INTO `cartpool`.`cart_pool` (`id`,`pool_id`,`name`,`neighborhood`,`description`,`zip`,`leader`,`creation_time`) VALUES (1,'pool1','Mypool','abc def','dwwd','33333',1,NULL);

INSERT INTO `cartpool`.`inventory` (`store_id`,`product_id`,`quantity`) VALUES (1,1,0);
INSERT INTO `cartpool`.`inventory` (`store_id`,`product_id`,`quantity`) VALUES (1,2,0);
INSERT INTO `cartpool`.`inventory` (`store_id`,`product_id`,`quantity`) VALUES (1,3,0);
INSERT INTO `cartpool`.`inventory` (`store_id`,`product_id`,`quantity`) VALUES (2,1,0);
INSERT INTO `cartpool`.`inventory` (`store_id`,`product_id`,`quantity`) VALUES (2,3,0);
INSERT INTO `cartpool`.`inventory` (`store_id`,`product_id`,`quantity`) VALUES (2,10,1);

INSERT INTO `cartpool`.`user` (`id`,`email`,`password`,`screen_name`,`nickname`,`pool_id`,`pool_status`,`contribution`,`role`,`active`,`token`,`creation_time`) VALUES (1,'test1@abc.com','$2a$10$qgsBp9ORd9O3EgUGumVcEeXElkG0sbr89C4euaB.jtz8yWBCZOlye','test1','test1',1,'ACTIVE',0,'ROLE_USER',1,NULL,'2020-05-15 11:06:18');
INSERT INTO `cartpool`.`user` (`id`,`email`,`password`,`screen_name`,`nickname`,`pool_id`,`pool_status`,`contribution`,`role`,`active`,`token`,`creation_time`) VALUES (2,'test2@abc.com','$2a$10$GQSUYZbfpImrdh72QHdVouVNbEWTqFsTt.ki86euSm1aFUFAjcSAa','test2','test2',NULL,NULL,0,'ROLE_USER',1,NULL,'2020-05-15 11:06:51');
INSERT INTO `cartpool`.`user` (`id`,`email`,`password`,`screen_name`,`nickname`,`pool_id`,`pool_status`,`contribution`,`role`,`active`,`token`,`creation_time`) VALUES (3,'test3@abc.com','$2a$10$giidPuvZ0OEBc1T5XWIVdOuv9cPK/rlhV82JBe49.JL/efoxAnIu2','test3','test3',NULL,NULL,0,'ROLE_USER',1,NULL,'2020-05-15 11:07:23');
INSERT INTO `cartpool`.`user` (`id`,`email`,`password`,`screen_name`,`nickname`,`pool_id`,`pool_status`,`contribution`,`role`,`active`,`token`,`creation_time`) VALUES (4,'admin@sjsu.edu','$2a$10$mQXSfLBX7HAW.MxEJpdq0OCPyhM5aiwscZt4p/JhVcHdALdp5kiHy','admin','admin',NULL,'INIT',0,'ROLE_ADMIN',1,NULL,'2020-05-15 11:15:58');



