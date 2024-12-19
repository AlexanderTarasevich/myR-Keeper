package by.tms.myRkeeper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Setter
@Getter
@jakarta.persistence.Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User waiter;

    private LocalDateTime orderTime;
    private String status;
    private Integer discount;
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Table table;


    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            total = total.add(item.getMenuItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }

    @PrePersist
    @PreUpdate
    public void calculateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            total = total.add(item.getMenuItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        this.totalPrice = total;
    }


    public BigDecimal getDiscountedTotal() {
        BigDecimal total = getTotalPrice();
        if (discount != null && discount > 0) {
            BigDecimal discountAmount = total.multiply(BigDecimal.valueOf(discount)).divide(BigDecimal.valueOf(100));
            return total.subtract(discountAmount);
        }
        return total;
    }
}