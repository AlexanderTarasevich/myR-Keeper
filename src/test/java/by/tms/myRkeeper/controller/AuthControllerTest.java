package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.MyRKeeperApplication;
import by.tms.myRkeeper.config.SecurityConfigTest;
import by.tms.myRkeeper.entity.User;
import by.tms.myRkeeper.repository.UserRepository;
import by.tms.myRkeeper.repository.RoleRepository;
import by.tms.myRkeeper.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
@ContextConfiguration(classes = {MyRKeeperApplication.class, SecurityConfigTest.class})
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testShowRegistrationPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoleForms(new ArrayList<>());  // Инициализируем поле roleForms

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/register")
                        .param("username", user.getUsername())
                        .param("password", user.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }


    @WithMockUser(username = "testuser", roles = "USER")
    @Test
    public void testHandleLoginSuccess() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(orderService.findOrdersByWaiter(Mockito.any(User.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/login-success"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPage"))
                .andExpect(model().attributeExists("orders"));
    }

}