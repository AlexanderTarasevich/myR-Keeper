package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.dto.SalesReport;
import by.tms.myRkeeper.entity.Order;
import by.tms.myRkeeper.entity.OrderItem;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        } else if ("salesReport".equals(choice)) {
            return "redirect:/admin/salesReport";
        } else {
            return "redirect:/admin/choose";
        }
    }

    @GetMapping("/salesReport")
    public String showSalesReport(Model model) {
        List<SalesReport> salesReports = orderService.getSalesReport();
        model.addAttribute("salesReports", salesReports);
        return "salesReport";
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

    @PostMapping("/setDiscount")
    public String setDiscount(@RequestParam Long orderId, @RequestParam int discount) {
        orderService.setDiscount(orderId, discount);
        return "redirect:/admin/orders";
    }


    @PostMapping("/close")
    public String closeOrder(@RequestParam Long orderId, RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
        String receiptContent = generateReceiptContent(order);
        try {
            Path path = Paths.get("customer_receipt.txt");
            Files.write(path, receiptContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error writing receipt file", e);
        }
        order.setStatus("CLOSED");
        order.getTable().setStatus("AVAILABLE");
        orderService.save(order);
        redirectAttributes.addFlashAttribute("message", "Чек для гостя напечатан и заказ закрыт");
        return "redirect:/admin/orders";
    }

    private String generateReceiptContent(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(order.getId()).append("\n");
        sb.append("Waiter: ").append(order.getWaiter().getUsername()).append("\n");
        sb.append("Order Time: ").append(order.getOrderTime()).append("\n");
        sb.append("Items:\n");
        for (OrderItem item : order.getOrderItems()) {
            sb.append(item.getMenuItem().getName()).append(" x ").append(item.getQuantity()).append(" - ").append(item.getComment()).append("\n");
        }
        sb.append("Total Price: ").append(order.getTotalPrice()).append("\n");
        if (order.getDiscount() != null && order.getDiscount() > 0) {
            sb.append("Discount: ").append(order.getDiscount()).append("%\n");
            sb.append("Price with Discount: ").append(order.getDiscountedTotal()).append("\n");
        }
        sb.append("Status: ").append(order.getStatus()).append("\n");
        return sb.toString();
    }

}