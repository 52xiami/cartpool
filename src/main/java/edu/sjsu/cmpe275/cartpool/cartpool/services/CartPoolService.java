package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.models.CartPool;

import java.util.List;

public interface CartPoolService {
    List<CartPool> getCartPools();
    CartPool getCartPool(Long id);
    CartPool createCartPool(CartPool cartPool);
    CartPool deleteCartPool(Long id);
}
