package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.entity.*;
import by.tms.myRkeeper.repository.MenuItemRepository;
import by.tms.myRkeeper.repository.TableRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final TableRepository tableRepository;

    @Autowired
    public OrderController(OrderService orderService, MenuItemRepository menuItemRepository, UserRepository userRepository, TableRepository tableRepository) {
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
    }

    @GetMapping
    public String viewOrders(Model model, Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User waiter = userRepository.findByUsername(principal.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Order> orders = orderService.findOrdersByWaiter(waiter);
        boolean hasClosedOrders = orders.stream().anyMatch(order -> "CLOSED".equals(order.getStatus()));
        List<MenuItem> barItems = menuItemRepository.findByCategory("bar");
        List<MenuItem> kitchenItems = menuItemRepository.findByCategory("kitchen");
        model.addAttribute("orders", orders);
        model.addAttribute("barItems", barItems);
        model.addAttribute("kitchenItems", kitchenItems);
        model.addAttribute("hasClosedOrders", hasClosedOrders);
        return "orderPage";
    }

    @GetMapping("/tables")
    public String viewTables(Model model) {
        List<Table> tables = tableRepository.findAll();
        model.addAttribute("tables", tables);
        return "chooseTable";
    }

    @GetMapping("/addItems")
    public String addItemsToOrderForm(@RequestParam Long orderId, Model model) {
        Order order = orderService.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
        List<MenuItem> menuItems = menuItemRepository.findAll();
        model.addAttribute("order", order);
        model.addAttribute("menuItems", menuItems);
        return "addItemsToOrder";
    }

    @PostMapping("/addItems")
    public String addItemsToOrder(@RequestParam Long orderId, @RequestParam("itemId") List<Long> itemIds, @RequestParam("quantity") List<Integer> quantities, @RequestParam("comments") List<String> comments, RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            Integer quantity = quantities.get(i);
            String comment = comments.get(i);
            if (quantity > 0) {
                MenuItem menuItem = menuItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + itemId));
                orderService.addItemToOrder(order, menuItem, comment, quantity);
            }
        }
        redirectAttributes.addFlashAttribute("message", "Items added to order successfully!");
        return "redirect:/orders";
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
    public String addItemToOrder(@RequestParam("itemId") List<Long> itemIds, @RequestParam("quantity") List<Integer> quantities, @RequestParam("comments") List<String> comments, @RequestParam("tableId") Long tableId, Model model, Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User waiter = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Table table = tableRepository.findById(tableId).orElseThrow(() -> new IllegalArgumentException("Invalid table ID: " + tableId));
        table.setStatus("OCCUPIED");
        tableRepository.save(table);

        Order order = orderService.createOrder(waiter, new ArrayList<>(), table);

        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            Integer quantity = quantities.get(i);
            String comment = comments.get(i);

            if (quantity > 0) {
                MenuItem menuItem = menuItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + itemId));
                orderService.addItemToOrder(order, menuItem, comment, quantity);
            }
        }

        model.addAttribute("message", "Items added to order successfully!");
        return "redirect:/orders";
    }

    @PostMapping("/print")
    public String printOrder(@RequestParam Long orderId, RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
        String receiptContent = generateReceiptContent(order);

        try {
            Path path = Paths.get("kitchen_receipt.txt");
            Files.write(path, receiptContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error writing receipt file", e);
        }

        order.setStatus("PRINTED");
        orderService.save(order);

        redirectAttributes.addFlashAttribute("message", "Чек для кухни/бара напечатан");
        return "redirect:/orders";
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
        return "redirect:/orders";
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
        sb.append("Status: ").append(order.getStatus()).append("\n");
        return sb.toString();
    }
}
