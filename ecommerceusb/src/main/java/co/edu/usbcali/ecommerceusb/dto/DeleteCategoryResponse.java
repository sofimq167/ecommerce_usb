package co.edu.usbcali.ecommerceusb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteCategoryResponse {
    private String message;
}