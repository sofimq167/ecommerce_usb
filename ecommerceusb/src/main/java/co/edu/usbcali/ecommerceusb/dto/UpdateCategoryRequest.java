package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String name;
    private Integer parentId;
}