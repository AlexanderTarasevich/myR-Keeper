package org.example.myrkeeper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {
    private Long menuItemId;
    private int quantity;
    private String comment;
}