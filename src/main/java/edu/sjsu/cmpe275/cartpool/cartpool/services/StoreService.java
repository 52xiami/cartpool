package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.models.Product;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Store;

import java.util.List;

public interface StoreService {
    List<Store> getStores();
    Store getStore(Long id);
    List<Product> getProducts(Long id);
    Store createStore(Store store);
    Store updateStore(Store store);
}
