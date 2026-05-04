package co.edu.usbcali.ecommerceusb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Integer id;
    private Integer cartId;
    private Integer productId;
    private String productName;
    private Integer quantity;
}