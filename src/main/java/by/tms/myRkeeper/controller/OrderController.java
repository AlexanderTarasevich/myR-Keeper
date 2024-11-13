package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.entity.MenuItem;
import by.tms.myRkeeper.entity.Order;
import by.tms.myRkeeper.entity.User;
import by.tms.myRkeeper.repository.MenuItemRepository;
import by.tms.myRkeeper.repository.UserRepository;
import by.tms.myRkeeper.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderController(OrderService orderService, MenuItemRepository menuItemRepository, UserRepository userRepository) {
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
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

    @PostMapping("/add")
    public String addItemToOrder(@RequestParam("itemId") List<Long> itemIds, @RequestParam("quantity") List<Integer> quantities, Model model, Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User waiter = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Order order = orderService.getCurrentOrderForWaiter(waiter);

        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            Integer quantity = quantities.get(i);

            if (quantity > 0) {
                MenuItem menuItem = menuItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + itemId));
                orderService.addItemToOrder(order, menuItem, "", quantity);
            }
        }

        model.addAttribute("message", "Items added to order successfully!");
        return "redirect:/orders";
    }

}
