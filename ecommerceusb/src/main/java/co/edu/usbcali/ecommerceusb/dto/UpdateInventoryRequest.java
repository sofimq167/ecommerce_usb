package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class UpdateInventoryRequest {
    private Integer productId;
    private Integer stock;
}