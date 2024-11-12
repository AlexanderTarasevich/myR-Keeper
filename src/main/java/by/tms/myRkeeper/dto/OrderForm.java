package org.example.myrkeeper.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderForm {
    private List<OrderItemForm> orderItems;
}
