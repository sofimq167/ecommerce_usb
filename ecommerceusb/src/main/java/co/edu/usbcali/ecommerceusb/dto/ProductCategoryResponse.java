package co.edu.usbcali.ecommerceusb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCategoryResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer categoryId;
    private String categoryName;
}