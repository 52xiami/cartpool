package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.models.Inventory;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();
    Product getProduct(Long id);
    List<Product> getProducts(Integer currentPage, Integer pageSize);
    Inventory addProductToStore(Long productId, Long storeID);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    Product deleteProduct(Long id);
}
