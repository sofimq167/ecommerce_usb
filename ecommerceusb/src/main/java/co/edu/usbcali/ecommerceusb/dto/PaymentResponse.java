package co.edu.usbcali.ecommerceusb.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class PaymentResponse {
    private Integer id;
    private Integer orderId;
    private String status;
    private String providerRef;
    private String idempotencyKey;
    private OffsetDateTime createdAt;
}