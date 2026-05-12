package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class UpdateCartRequest {
    private String status;
    private Integer userId;
}