package by.tms.myRkeeper.service;

import by.tms.myRkeeper.entity.User;
import by.tms.myRkeeper.entity.MenuItem;
import by.tms.myRkeeper.entity.Order;
import by.tms.myRkeeper.entity.OrderItem;
import by.tms.myRkeeper.repository.MenuItemRepository;
import by.tms.myRkeeper.repository.OrderItemRepository;
import by.tms.myRkeeper.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Order createOrder(User waiter, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setWaiter(waiter);
        order.setOrderTime(LocalDateTime.now());
        order.setOrderItems(orderItems);
        order.setStatus("OPEN");
        return orderRepository.save(order);
    }

    public Order getCurrentOrderForWaiter(User waiter) {
        return orderRepository.findByWaiterAndStatus(waiter, "OPEN").orElseGet(() -> createOrder(waiter, new ArrayList<>()));
    }

    public void addItemToOrder(Order order, MenuItem menuItem, String comment, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setComment(comment);
        orderItem.setQuantity(quantity);
        orderItemRepository.save(orderItem);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
