package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateInventoryRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteInventoryResponse;
import co.edu.usbcali.ecommerceusb.dto.InventoryResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateInventoryRequest;
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
        List<Inventory> inventories = inventoryRepository.findAll();
        if (inventories.isEmpty()) {
            return List.of();
        }
        return InventoryMapper.modelToInventoryResponseList(inventories);
    }

    @Override
    public InventoryResponse getInventoryById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Inventario no encontrado con el id: %d", id)));
        return InventoryMapper.modelToInventoryResponse(inventory);
    }

    @Override
    public InventoryResponse createInventory(CreateInventoryRequest createInventoryRequest) throws Exception {
        if (Objects.isNull(createInventoryRequest)) {
            throw new Exception("El objeto createInventoryRequest no puede ser nulo.");
        }
        if (createInventoryRequest.getProductId() == null || createInventoryRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }
        if (createInventoryRequest.getStock() == null || createInventoryRequest.getStock() < 0) {
            throw new Exception("El campo stock no puede ser nulo ni negativo.");
        }

        Product product = productRepository.findById(createInventoryRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        // Validar que no exista ya un inventario para ese producto
        if (inventoryRepository.existsByProductId(createInventoryRequest.getProductId())) {
            throw new Exception("Ya existe un inventario para ese producto.");
        }

        Inventory inventory = InventoryMapper.createInventoryRequestToInventory(
                createInventoryRequest, product);
        inventory = inventoryRepository.save(inventory);
        return InventoryMapper.modelToInventoryResponse(inventory);
    }

    @Override
    public InventoryResponse updateInventory(Integer id, UpdateInventoryRequest updateInventoryRequest) throws Exception {
        // Validar id
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para actualizar");
        }

        // Validar que el inventario existe
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Inventario no encontrado con el id: %d", id)));

        // Validar campos del request
        if (Objects.isNull(updateInventoryRequest)) {
            throw new Exception("El objeto createInventoryRequest no puede ser nulo.");
        }
        if (updateInventoryRequest.getProductId() == null ||
                updateInventoryRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }
        if (updateInventoryRequest.getStock() == null ||
                updateInventoryRequest.getStock() < 0) {
            throw new Exception("El campo stock no puede ser nulo ni negativo.");
        }

        // Validar que el producto existe
        Product product = productRepository.findById(updateInventoryRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        // Validar que el producto no lo tenga otro inventario diferente al que estamos actualizando
        if (inventoryRepository.existsByProductId(updateInventoryRequest.getProductId()) &&
                !inventory.getProduct().getId().equals(updateInventoryRequest.getProductId())) {
            throw new Exception("Ya existe un inventario para ese producto.");
        }

        // Actualizar campos
        inventory.setProduct(product);
        inventory.setStock(updateInventoryRequest.getStock());
        inventory.setUpdatedAt(OffsetDateTime.now());

        inventory = inventoryRepository.save(inventory);
        return InventoryMapper.modelToInventoryResponse(inventory);
    }

    @Override
    public DeleteInventoryResponse deleteInventory(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para eliminar");
        }

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Inventario no encontrado con el id: %d", id)));

        inventoryRepository.delete(inventory);

        return DeleteInventoryResponse.builder()
                .message(String.format("Inventario con id %d eliminado correctamente", id))
                .build();
    }
}