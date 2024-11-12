package org.example.myrkeeper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private MenuItem menuItem;
    private String comment;
    private int quantity;

    public OrderItem() {
        this.quantity = 1;
    }

}
