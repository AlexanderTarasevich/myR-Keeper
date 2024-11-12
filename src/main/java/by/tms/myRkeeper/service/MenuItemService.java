package org.example.myrkeeper.service;

import org.example.myrkeeper.entity.MenuItem;
import org.example.myrkeeper.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> findById(Long id) {
        return menuItemRepository.findById(id);
    }

    public MenuItem save(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }
}
