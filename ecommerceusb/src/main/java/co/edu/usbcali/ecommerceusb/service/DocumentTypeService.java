package co.edu.usbcali.ecommerceusb.service;

import co.edu.usbcali.ecommerceusb.dto.CreateDocumentTypeRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteDocumentTypeResponse;
import co.edu.usbcali.ecommerceusb.dto.DocumentTypeResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateDocumentTypeRequest;

import java.util.List;

public interface DocumentTypeService {
    List<DocumentTypeResponse> getDocumentTypes();
    DocumentTypeResponse getDocumentTypeById(Integer id);
    DocumentTypeResponse createDocumentType(CreateDocumentTypeRequest request);
    DocumentTypeResponse updateDocumentType(Integer id, UpdateDocumentTypeRequest request);
    DeleteDocumentTypeResponse deleteDocumentType(Integer id);
}