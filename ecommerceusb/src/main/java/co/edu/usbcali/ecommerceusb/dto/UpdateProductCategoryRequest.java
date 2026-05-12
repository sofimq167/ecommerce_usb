package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class UpdateProductCategoryRequest {
    private Integer productId;
    private Integer categoryId;
}