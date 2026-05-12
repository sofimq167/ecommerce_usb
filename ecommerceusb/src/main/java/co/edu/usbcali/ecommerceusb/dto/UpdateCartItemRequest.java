package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
}