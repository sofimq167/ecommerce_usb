package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class CreateCartRequest {
    private String status;
    private Integer userId;
}