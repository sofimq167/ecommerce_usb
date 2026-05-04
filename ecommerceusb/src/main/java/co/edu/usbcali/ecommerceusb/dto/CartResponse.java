package co.edu.usbcali.ecommerceusb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponse {
    private Integer id;
    private String status;
    private Integer userId;
    private String userFullName;
}