package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateOrderItemRequest {
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal unitPriceSnapshot;
    private BigDecimal lineTotal;
}