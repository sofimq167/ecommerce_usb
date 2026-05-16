package co.edu.usbcali.ecommerceusb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteOrderResponse {
    private String message;
}