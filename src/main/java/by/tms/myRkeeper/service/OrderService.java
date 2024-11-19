package by.tms.myRkeeper.service;

import by.tms.myRkeeper.entity.*;
import by.tms.myRkeeper.repository.MenuItemRepository;
import by.tms.myRkeeper.repository.OrderItemRepository;
import by.tms.myRkeeper.repository.OrderRepository;
import by.tms.myRkeeper.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final TableRepository tableRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, MenuItemRepository menuItemRepository, TableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuItemRepository = menuItemRepository;
        this.tableRepository = tableRepository;
    }

    public Order createOrder(User waiter, List<OrderItem> orderItems, Table table) {
        Order order = new Order();
        order.setWaiter(waiter);
        order.setOrderTime(LocalDateTime.now());
        order.setOrderItems(orderItems);
        order.setStatus("OPEN");
        order.setTable(table);
        return orderRepository.save(order);
    }

    public List<Order> findOrdersByWaiter(User waiter) {
        return orderRepository.findByWaiter(waiter);
    }

    public OrderItem addItemToOrder(Order order, MenuItem menuItem, String comment, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setComment(comment);
        orderItem.setQuantity(quantity);
        return orderItemRepository.save(orderItem);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void setDiscount(Long orderId, int discount) {
        Order order = findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
        order.setDiscount(discount);
        save(order);
    }
}
