package co.edu.usbcali.ecommerceusb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryMovementResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer orderId;
    private Integer qty;
    private String type;
}