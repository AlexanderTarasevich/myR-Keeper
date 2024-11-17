package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.entity.Order;
import by.tms.myRkeeper.repository.MenuItemRepository;
import by.tms.myRkeeper.repository.TableRepository;
import by.tms.myRkeeper.repository.UserRepository;
import by.tms.myRkeeper.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final TableRepository tableRepository;

    @Autowired
    public AdminController(OrderService orderService, MenuItemRepository menuItemRepository, UserRepository userRepository, TableRepository tableRepository) {
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
    }

    @GetMapping("/choose")
    public String showChoosePage() {
        return "adminChoose";
    }

    @PostMapping("/choose")
    public String handleChoice(@RequestParam("choice") String choice) {
        if ("newOrder".equals(choice)) {
            return "redirect:/choose";
        } else if ("viewOrders".equals(choice)) {
            return "redirect:/admin/orders";
        } else {
            return "redirect:/admin/choose";
        }
    }

    @GetMapping("/orders")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "adminOrderPage";
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(@RequestParam Long orderId) {
        orderService.deleteById(orderId);
        return "redirect:/admin/orders";
    }
}
