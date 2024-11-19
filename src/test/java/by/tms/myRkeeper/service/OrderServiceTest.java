package by.tms.myRkeeper.service;


import by.tms.myRkeeper.entity.*;
import by.tms.myRkeeper.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        User waiter = new User();
        waiter.setUsername("testWaiter");

        Table table = new Table();
        table.setNumber(1);

        Order order = new Order();
        order.setWaiter(waiter);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("OPEN");
        order.setTable(table);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(waiter, new ArrayList<>(), table);

        assertNotNull(createdOrder);
        assertEquals("testWaiter", createdOrder.getWaiter().getUsername());
        assertEquals("OPEN", createdOrder.getStatus());
        assertEquals(Integer.valueOf(1), createdOrder.getTable().getNumber());
    }

    @Test
    public void testSetDiscount() {
        Order order = new Order();
        order.setId(1L);
        List<OrderItem> orderItems = new ArrayList<>();
        MenuItem menuItem = new MenuItem();
        menuItem.setPrice(new BigDecimal("100.00"));
        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(1);
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);


        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderService.setDiscount(1L, 10);

        assertEquals(Integer.valueOf(10), order.getDiscount());
        assertEquals(new BigDecimal("90.00"), order.getDiscountedTotal());
    }


}


