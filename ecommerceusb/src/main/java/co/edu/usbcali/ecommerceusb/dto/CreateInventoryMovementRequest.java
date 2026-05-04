package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class CreateInventoryMovementRequest {
    private Integer productId;
    private Integer orderId;
    private Integer qty;
    private String type;
}