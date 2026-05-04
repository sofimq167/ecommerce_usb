package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String name;
    private Integer productId;
}