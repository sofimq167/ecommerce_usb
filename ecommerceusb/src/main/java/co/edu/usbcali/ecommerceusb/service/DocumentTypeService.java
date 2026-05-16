package co.edu.usbcali.ecommerceusb.service;

import co.edu.usbcali.ecommerceusb.dto.CreateDocumentTypeRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteDocumentTypeResponse;
import co.edu.usbcali.ecommerceusb.dto.DocumentTypeResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateDocumentTypeRequest;

import java.util.List;

public interface DocumentTypeService {
    List<DocumentTypeResponse> getDocumentTypes();
    DocumentTypeResponse getDocumentTypeById(Integer id) throws Exception;
    DocumentTypeResponse createDocumentType(CreateDocumentTypeRequest request) throws Exception;
    DocumentTypeResponse updateDocumentType(Integer id, UpdateDocumentTypeRequest request) throws Exception;
    DeleteDocumentTypeResponse deleteDocumentType(Integer id) throws Exception;
}