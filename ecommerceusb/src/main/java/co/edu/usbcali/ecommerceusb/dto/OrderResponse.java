package co.edu.usbcali.ecommerceusb.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class OrderResponse {
    private Integer id;
    private Integer userId;
    private String userFullName;
    private String status;
    private BigDecimal totalAmount;
    private String currency;
    private OffsetDateTime createdAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime cancelledAt;
}