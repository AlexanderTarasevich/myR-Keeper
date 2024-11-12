package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.dto.RoleForm;
import by.tms.myRkeeper.entity.Role;
import by.tms.myRkeeper.entity.User;
import by.tms.myRkeeper.repository.RoleRepository;
import by.tms.myRkeeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> userRoles = new HashSet<>();
        for (RoleForm roleForm : user.getRoleForms()) {
            Role existingRole = roleRepository.findByName(roleForm.getRoleName()).orElse(null);
            if (existingRole != null) {
                userRoles.add(existingRole);
            } else { // Создать и сохранить новую роль, если она не существует
            Role newRole = new Role();
                newRole.setName(roleForm.getRoleName());
                roleRepository.save(newRole);
                userRoles.add(newRole);
            }
        }
        user.setRoles(userRoles);
        userRepository.save(user);
        return "redirect:/login";
    }
}

