package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateOrderRequest {
    private Integer userId;
    private String status;
    private BigDecimal totalAmount;
    private String currency;
}