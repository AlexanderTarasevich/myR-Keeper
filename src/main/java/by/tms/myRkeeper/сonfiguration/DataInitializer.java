//package by.tms.myRkeeper.сonfiguration;
//
//
//import jakarta.annotation.PostConstruct;
//import by.tms.myRkeeper.entity.Role;
//import by.tms.myRkeeper.entity.User;
//import by.tms.myRkeeper.repository.RoleRepository;
//import by.tms.myRkeeper.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Component
//public class DataInitializer {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostConstruct
//    public void init() {
//        // Создаем роли
//        Role adminRole = new Role();
//        adminRole.setName("ROLE_ADMIN");
//        roleRepository.save(adminRole);
//
//        Role waiterRole = new Role();
//        waiterRole.setName("ROLE_WAITER");
//        roleRepository.save(waiterRole);
//
//        // Создаем пользователя-администратора
//        User admin = new User();
//        admin.setUsername("admin");
//        admin.setPassword(passwordEncoder.encode("adminpassword"));
//        Set<Role> adminRoles = new HashSet<>();
//        adminRoles.add(adminRole);
//        admin.setRoles(adminRoles);
//        userRepository.save(admin);
//
//        // Создаем пользователя-официанта
//        User waiter = new User();
//        waiter.setUsername("waiter");
//        waiter.setPassword(passwordEncoder.encode("waiterpassword"));
//        Set<Role> waiterRoles = new HashSet<>();
//        waiterRoles.add(waiterRole);
//        waiter.setRoles(waiterRoles);
//        userRepository.save(waiter);
//    }
//}
//
//
