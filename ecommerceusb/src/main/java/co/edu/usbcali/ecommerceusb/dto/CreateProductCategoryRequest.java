package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class CreateProductCategoryRequest {
    private Integer productId;
    private Integer categoryId;
}