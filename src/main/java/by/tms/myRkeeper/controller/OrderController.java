package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.entity.Order;
import by.tms.myRkeeper.service.OrderService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String viewOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orderPage";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam Long orderId, Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            orderService.deleteById(orderId);
        } else {
            throw new AccessDeniedException("You do not have permission to delete orders");
        }
        return "redirect:/orders";
    }
}