package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateDocumentTypeRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteDocumentTypeResponse;
import co.edu.usbcali.ecommerceusb.dto.DocumentTypeResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateDocumentTypeRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.DocumentTypeMapper;
import co.edu.usbcali.ecommerceusb.model.DocumentType;
import co.edu.usbcali.ecommerceusb.repository.DocumentTypeRepository;
import co.edu.usbcali.ecommerceusb.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Override
    public List<DocumentTypeResponse> getDocumentTypes() {
        try {
            List<DocumentType> documentTypes = documentTypeRepository.findAll();
            if (documentTypes.isEmpty()) {
                return List.of();
            }
            return DocumentTypeMapper.modelToDocumentTypeResponseList(documentTypes);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener los tipos de documento: " + e.getMessage());
        }
    }

    @Override
    public DocumentTypeResponse getDocumentTypeById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Tipo de documento no encontrado con el id: %d", id)));
        return DocumentTypeMapper.modelToDocumentTypeResponse(documentType);
    }

    @Override
    public DocumentTypeResponse createDocumentType(CreateDocumentTypeRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException("El objeto request no puede ser nulo.");
        }
        if (Objects.isNull(request.getCode()) || request.getCode().isBlank()) {
            throw new BadRequestException("El campo code no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(request.getName()) || request.getName().isBlank()) {
            throw new BadRequestException("El campo name no puede ser nulo ni vacío.");
        }

        DocumentType documentType = DocumentType.builder()
                .code(request.getCode())
                .name(request.getName())
                .createdAt(OffsetDateTime.now())
                .build();

        documentType = documentTypeRepository.save(documentType);
        return DocumentTypeMapper.modelToDocumentTypeResponse(documentType);
    }

    @Override
    public DocumentTypeResponse updateDocumentType(Integer id, UpdateDocumentTypeRequest request) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Tipo de documento no encontrado con el id: %d", id)));
        if (Objects.isNull(request)) {
            throw new BadRequestException("El objeto request no puede ser nulo.");
        }
        if (Objects.isNull(request.getCode()) || request.getCode().isBlank()) {
            throw new BadRequestException("El campo code no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(request.getName()) || request.getName().isBlank()) {
            throw new BadRequestException("El campo name no puede ser nulo ni vacío.");
        }

        documentType.setCode(request.getCode());
        documentType.setName(request.getName());

        documentType = documentTypeRepository.save(documentType);
        return DocumentTypeMapper.modelToDocumentTypeResponse(documentType);
    }

    @Override
    public DeleteDocumentTypeResponse deleteDocumentType(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Tipo de documento no encontrado con el id: %d", id)));

        documentTypeRepository.delete(documentType);
        return DeleteDocumentTypeResponse.builder()
                .message(String.format("Tipo de documento con id %d eliminado correctamente", id))
                .build();
    }
}