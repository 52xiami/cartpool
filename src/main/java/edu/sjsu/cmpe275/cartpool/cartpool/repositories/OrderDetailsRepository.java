package edu.sjsu.cmpe275.cartpool.cartpool.repositories;

import edu.sjsu.cmpe275.cartpool.cartpool.models.OrderDetails;
import edu.sjsu.cmpe275.cartpool.cartpool.models.OrderDetailsId;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsId> {
    List<OrderDetails> findAllByProduct(Product product);
}
