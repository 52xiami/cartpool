package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.NotFoundException;
import edu.sjsu.cmpe275.cartpool.cartpool.models.*;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.OrderDetailsRepository;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.OrderRepository;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final UserService userService;
    private final MailService mailService;
    private final UserRepository userRepository;

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrder(Long id) {
        return null;
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Transactional
    @Override
    public Order createOrder(Order order) {

        List<OrderDetails> orderDetails = order.getOrderDetails();

        double amount = 0;


        for (OrderDetails d:orderDetails) {
            double price = d.getPrice();
            int quantity = d.getQuantity();
            double taxfee = price * quantity * 0.0975;
            amount = amount + price * quantity * 1.0975;
            d.setTaxFee(taxfee);
        }


        BigDecimal bdAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        order.setAmount(bdAmount.doubleValue());

        order.setOrderDetails(null);
        order.setStatus(OrderStatus.CREATED);
        Order newOrder = orderRepository.save(order);
        Long orderId = newOrder.getId();

        for (OrderDetails d:orderDetails) {
            d.setOrderId(orderId);
        }
        
        orderDetailsRepository.saveAll(orderDetails);

        User user = userRepository.findById(order.getUserId()).get();
        String subject = "Order Created";
        String content = "<p>Your order("+ order.getId()+") has been created.</p>";
        mailService.sendHtmlMail(user.getEmail(),subject, content);

        return newOrder;
    }

    @Override
    public List<Order> getPoolOrders(Long userId, Long poolId) {
        return orderRepository.findAllByPoolIdAndStatusAndPickupMethodAndUserIdNotAndPickupUserIsNullAndDueTimeGreaterThan(
                poolId,OrderStatus.CREATED, PickupMethod.OTHER, userId, LocalDateTime.now());
    }

    @Override
    public List<Order> getPickupOrdersByUser(Long userId) {

        return orderRepository.findAllByPickupUserAndStatus(userId, OrderStatus.CREATED);

    }

    @Override
    public List<Order> getDeliverOrdersByUser(Long userId) {
        return orderRepository.findAllByPickupUserAndStatus(userId, OrderStatus.PICKED);
    }


    @Override
    public Order setPickupUser(Long orderId, Long userId) {
        if(orderRepository.findById(orderId).isPresent()){
            Order order = orderRepository.findById(orderId).get();
            order.setPickupUser(userId);
            orderRepository.save(order);

            User user = userRepository.findById(order.getUserId()).get();
            User pickupUser = userRepository.findById(userId).get();
            String subject = "Order Pick Up";
            String content = "<p>Your order("+ order.getId()+") will be picked up by "+pickupUser.getScreenName()+".</p>";
            mailService.sendHtmlMail(user.getEmail(),subject, content);

            String pickupSubject = "Order Pick Up";
            String pickupContent = "<p>Your will pick up order("+ order.getId()+") for others.</p>";
            mailService.sendHtmlMail(pickupUser.getEmail(),pickupSubject, pickupContent);

            return order;
        }else {
            throw new NotFoundException("Cannot find order");
        }
    }


    @Override
    public Order setOrderCheckout(Long orderId) {

        if(orderRepository.findById(orderId).isPresent()){
            Order order = orderRepository.findById(orderId).get();
            order.setStatus(OrderStatus.CHECKEDOUT);
            order.setCheckoutTime(LocalDateTime.now());

            return orderRepository.save(order);
        }else {
            throw new NotFoundException("Cannot find order");
        }
    }

    @Override
    public Order setOrderPickup(Long orderId) {
        if(orderRepository.findById(orderId).isPresent()){
            Order order = orderRepository.findById(orderId).get();
            if(order.getPickupMethod().equals(PickupMethod.SELF)){
                order.setStatus(OrderStatus.DELIVERED);
                order.setPickupTime(LocalDateTime.now());
                order.setDeliveredTime(LocalDateTime.now());

                orderRepository.save(order);

                User user = userRepository.findById(order.getUserId()).get();
                String subject = "Order Delivered";
                String content = "<p>Your order("+ order.getId()+") has been delivered.</p>";
                mailService.sendHtmlMail(user.getEmail(),subject, content);

            }else{
                order.setStatus(OrderStatus.PICKED);
                order.setPickupTime(LocalDateTime.now());

                orderRepository.save(order);

                User user = userRepository.findById(order.getUserId()).get();
                User pickupUser = userRepository.findById(order.getPickupUser()).get();
                String subject = "Order Picked Up";
                String content = "<p>Your order("+ order.getId()+") has been picked up by "+pickupUser.getScreenName()+".</p>";
                mailService.sendHtmlMail(user.getEmail(),subject, content);

                String pickupSubject = "Order Picked Up";
                String pickupContent = "<p>Your have picked up order("+ order.getId()+") for others.</p>";
                pickupContent += "<p>Order Details: "+ order.getOrderDetails().toString() +"</p>";
                pickupContent += "<p>Shipping address is "+ order.getStreet()+", "+ order.getCity()+", "+order.getState()+
                        ", "+ order.getZip()+"</p>";
                mailService.sendHtmlMail(pickupUser.getEmail(),pickupSubject, pickupContent);

            }
            return order;
        }else {
            throw new NotFoundException("Cannot find order");
        }
    }

    @Transactional
    @Override
    public Order setOrderDeliver(Long orderId) {
        if(orderRepository.findById(orderId).isPresent()){
            Order order = orderRepository.findById(orderId).get();
            order.setStatus(OrderStatus.DELIVERED);
            order.setDeliveredTime(LocalDateTime.now());

            if(order.getPickupMethod().equals(PickupMethod.OTHER)){
                User pickupUser = userService.getUser(order.getPickupUser());
                userService.setContributionScore(order.getPickupUser(),pickupUser.getContribution()+1);

                User orderUser = userService.getUser(order.getUserId());
                userService.setContributionScore(order.getUserId(),orderUser.getContribution()-1);

            }

            orderRepository.save(order);

            User user = userRepository.findById(order.getUserId()).get();
            String subject = "Order Delivered";
            String content = "<p>Your order("+ order.getId()+") has been delivered.</p>";
            mailService.sendHtmlMail(user.getEmail(),subject, content);

            return order;
        }else {
            throw new NotFoundException("Cannot find order");
        }
    }
}
