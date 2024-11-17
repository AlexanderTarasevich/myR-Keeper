package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.entity.MenuItem;
import by.tms.myRkeeper.entity.Table;
import by.tms.myRkeeper.repository.MenuItemRepository;
import by.tms.myRkeeper.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MenuController {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/choose")
    public String showChoosePage() {
        return "choose";
    }

    @PostMapping("/choose")
    public String handleChoice(@RequestParam("choice") String choice) {
        if ("bar".equals(choice)) {
            return "redirect:/menu/bar";
        } else if ("kitchen".equals(choice)) {
            return "redirect:/menu/kitchen";
        } else {
            return "redirect:/choose";
        }
    }

    @GetMapping("/menu/bar")
    public String showBarMenu(Model model) {
        List<MenuItem> barItems = menuItemRepository.findByCategory("bar");
        List<Table> tables = tableRepository.findAll();
        model.addAttribute("menuType", "Bar");
        model.addAttribute("menuItems", barItems);
        model.addAttribute("tables", tables);
        return "menu";
    }

    @GetMapping("/menu/kitchen")
    public String showKitchenMenu(Model model) {
        List<MenuItem> kitchenItems = menuItemRepository.findByCategory("kitchen");
        List<Table> tables = tableRepository.findAll();
        model.addAttribute("menuType", "Kitchen");
        model.addAttribute("menuItems", kitchenItems);
        model.addAttribute("tables", tables);
        return "menu";
    }

}
