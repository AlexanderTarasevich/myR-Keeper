package by.tms.myRkeeper.controller;

import by.tms.myRkeeper.MyRKeeperApplication;
import by.tms.myRkeeper.config.SecurityConfigTest;
import by.tms.myRkeeper.entity.Order;
import by.tms.myRkeeper.entity.Table;
import by.tms.myRkeeper.entity.User;
import by.tms.myRkeeper.repository.MenuItemRepository;
import by.tms.myRkeeper.repository.TableRepository;
import by.tms.myRkeeper.repository.UserRepository;
import by.tms.myRkeeper.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminController.class)
@ContextConfiguration(classes = {MyRKeeperApplication.class, SecurityConfigTest.class})
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MenuItemRepository menuItemRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TableRepository tableRepository;


    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test

    public void testShowChoosePage() throws Exception {
        mockMvc.perform(get("/admin/choose"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminChoose"));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testHandleChoice() throws Exception {
        mockMvc.perform(post("/admin/choose")
                        .param("choice", "newOrder"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/choose"));

        mockMvc.perform(post("/admin/choose")
                        .param("choice", "viewOrders"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testViewAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(1L);
        Table table = new Table();
        table.setNumber(1);
        order.setTable(table);
        User waiter = new User();
        waiter.setUsername("testWaiter");
        order.setWaiter(waiter);
        orders.add(order);
        Mockito.when(orderService.findAll()).thenReturn(orders);
        mockMvc.perform(get("/admin/orders")).andExpect(status().isOk()).andExpect(view().name("adminOrderPage")).andExpect(model().attribute("orders", orders));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testDeleteOrder() throws Exception {
        mockMvc.perform(post("/admin/deleteOrder")
                        .param("orderId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));

        Mockito.verify(orderService, Mockito.times(1)).deleteById(1L);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testSetDiscount() throws Exception {
        mockMvc.perform(post("/admin/setDiscount")
                        .param("orderId", "1")
                        .param("discount", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));

        Mockito.verify(orderService, Mockito.times(1)).setDiscount(1L, 10);
    }
}
