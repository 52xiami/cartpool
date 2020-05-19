package edu.sjsu.cmpe275.cartpool.cartpool.repositories;

import edu.sjsu.cmpe275.cartpool.cartpool.models.Order;
import edu.sjsu.cmpe275.cartpool.cartpool.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
    List<Order> findAllByPickupUserAndStatus(Long userId, String status);
    List<Order> findAllByPoolIdAndStatusAndPickupMethodAndUserIdNotAndPickupUserIsNullAndDueTimeGreaterThan(
            Long poolId, String status, String pickupMethod, Long userId, LocalDateTime now);
    List<Order> findAllByUserIdAndPoolIdAndStatusNot(Long userId, Long poolId, String status);
    List<Order> findAllByOrderDetailsAndStatusNot(OrderDetails orderDetails, String status);
    List<Order> findAllByStoreIdAndStatusNot(Long storeId, String status);
}
