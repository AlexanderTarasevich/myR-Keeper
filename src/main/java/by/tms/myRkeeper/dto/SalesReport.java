package by.tms.myRkeeper.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SalesReport {
    private String waiterName;
    private BigDecimal totalSales;
    private Long ordersCount;

    public SalesReport(String waiterName, BigDecimal totalSales, Long ordersCount) {
        this.waiterName = waiterName;
        this.totalSales = totalSales;
        this.ordersCount = ordersCount;
    }


}
