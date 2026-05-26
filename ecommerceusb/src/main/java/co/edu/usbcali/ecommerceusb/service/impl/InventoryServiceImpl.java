package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateInventoryRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteInventoryResponse;
import co.edu.usbcali.ecommerceusb.dto.InventoryResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateInventoryRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.InventoryMapper;
import co.edu.usbcali.ecommerceusb.model.Inventory;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.InventoryRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<InventoryResponse> getInventories() {
        try {
            List<Inventory> inventories = inventoryRepository.findAll();
            if (inventories.isEmpty()) {
                return List.of();
            }
            return InventoryMapper.modelToInventoryResponseList(inventories);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener los inventarios: " + e.getMessage());
        }
    }

    @Override
    public InventoryResponse getInventoryById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Inventario no encontrado con el id: %d", id)));
        return InventoryMapper.modelToInventoryResponse(inventory);
    }

    @Override
    public InventoryResponse createInventory(CreateInventoryRequest createInventoryRequest) {
        if (Objects.isNull(createInventoryRequest)) {
            throw new BadRequestException("El objeto createInventoryRequest no puede ser nulo.");
        }
        if (createInventoryRequest.getProductId() == null ||
                createInventoryRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }
        if (createInventoryRequest.getStock() == null ||
                createInventoryRequest.getStock() < 0) {
            throw new BadRequestException("El campo stock no puede ser nulo ni negativo.");
        }

        Product product = productRepository.findById(createInventoryRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        if (inventoryRepository.existsByProductId(createInventoryRequest.getProductId())) {
            throw new BadRequestException("Ya existe un inventario para ese producto.");
        }

        Inventory inventory = InventoryMapper.createInventoryRequestToInventory(
                createInventoryRequest, product);
        inventory = inventoryRepository.save(inventory);
        return InventoryMapper.modelToInventoryResponse(inventory);
    }

    @Override
    public InventoryResponse updateInventory(Integer id, UpdateInventoryRequest updateInventoryRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Inventario no encontrado con el id: %d", id)));
        if (Objects.isNull(updateInventoryRequest)) {
            throw new BadRequestException("El objeto updateInventoryRequest no puede ser nulo.");
        }
        if (updateInventoryRequest.getProductId() == null ||
                updateInventoryRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }
        if (updateInventoryRequest.getStock() == null ||
                updateInventoryRequest.getStock() < 0) {
            throw new BadRequestException("El campo stock no puede ser nulo ni negativo.");
        }

        Product product = productRepository.findById(updateInventoryRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        if (inventoryRepository.existsByProductId(updateInventoryRequest.getProductId()) &&
                !inventory.getProduct().getId().equals(updateInventoryRequest.getProductId())) {
            throw new BadRequestException("Ya existe un inventario para ese producto.");
        }

        inventory.setProduct(product);
        inventory.setStock(updateInventoryRequest.getStock());
        inventory.setUpdatedAt(OffsetDateTime.now());

        inventory = inventoryRepository.save(inventory);
        return InventoryMapper.modelToInventoryResponse(inventory);
    }

    @Override
    public DeleteInventoryResponse deleteInventory(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Inventario no encontrado con el id: %d", id)));

        inventoryRepository.delete(inventory);
        return DeleteInventoryResponse.builder()
                .message(String.format("Inventario con id %d eliminado correctamente", id))
                .build();
    }
}