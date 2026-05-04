package co.edu.usbcali.ecommerceusb.controllers;

import co.edu.usbcali.ecommerceusb.dto.CreateDocumentTypeRequest;
import co.edu.usbcali.ecommerceusb.dto.DocumentTypeResponse;
import co.edu.usbcali.ecommerceusb.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document-type")
public class DocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;

    @GetMapping("/all")
    public ResponseEntity<List<DocumentTypeResponse>> getAll() {
        List<DocumentTypeResponse> documentTypes = documentTypeService.getDocumentTypes();
        return new ResponseEntity<>(documentTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentTypeResponse> getById(@PathVariable Integer id) throws Exception {
        DocumentTypeResponse documentTypeResponse = documentTypeService.getDocumentTypeById(id);
        return new ResponseEntity<>(documentTypeResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DocumentTypeResponse> createDocumentType(
            @RequestBody CreateDocumentTypeRequest request) throws Exception {
        return new ResponseEntity<>(documentTypeService.createDocumentType(request), HttpStatus.CREATED);
    }
}