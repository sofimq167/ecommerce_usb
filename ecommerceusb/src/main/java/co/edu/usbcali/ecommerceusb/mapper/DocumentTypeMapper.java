package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.DocumentTypeResponse;
import co.edu.usbcali.ecommerceusb.model.DocumentType;

import java.util.List;

public class DocumentTypeMapper {
    public static DocumentTypeResponse modelToDocumentTypeResponse(DocumentType documentType){
        return DocumentTypeResponse.builder()
                .id(documentType.getId())
                .code(documentType.getCode())
                .name(documentType.getName())
                .build();
    }
    public static List<DocumentTypeResponse> modelToDocumentTypeResponseList(List<DocumentType> documentTypes) {
        return documentTypes.stream().map(DocumentTypeMapper::modelToDocumentTypeResponse).toList();
    }
}
