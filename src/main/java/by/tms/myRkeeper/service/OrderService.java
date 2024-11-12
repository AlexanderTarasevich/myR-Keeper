package org.example.myrkeeper.service;

import org.example.myrkeeper.entity.MenuItem;
import org.example.myrkeeper.entity.Order;
import org.example.myrkeeper.entity.OrderItem;
import org.example.myrkeeper.entity.User;
import org.example.myrkeeper.repository.MenuItemRepository;
import org.example.myrkeeper.repository.OrderItemRepository;
import org.example.myrkeeper.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        return orderRepository.save(order);
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
}