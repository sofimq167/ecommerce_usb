package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class CreateInventoryRequest {
    private Integer productId;
    private Integer stock;
}