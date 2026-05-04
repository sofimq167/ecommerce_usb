package co.edu.usbcali.ecommerceusb.dto;

import lombok.Data;

@Data
public class CreateDocumentTypeRequest {
    private String code;
    private String name;
}