package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateInventoryMovementRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteInventoryMovementResponse;
import co.edu.usbcali.ecommerceusb.dto.InventoryMovementResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateInventoryMovementRequest;
import co.edu.usbcali.ecommerceusb.mapper.InventoryMovementMapper;
import co.edu.usbcali.ecommerceusb.model.InventoryMovement;
import co.edu.usbcali.ecommerceusb.model.InventoryMovementType;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.InventoryMovementRepository;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.InventoryMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class InventoryMovementServiceImpl implements InventoryMovementService {

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<InventoryMovementResponse> getInventoryMovements() {
        List<InventoryMovement> inventoryMovements = inventoryMovementRepository.findAll();
        if (inventoryMovements.isEmpty()) {
            return List.of();
        }
        return InventoryMovementMapper.modelToInventoryMovementResponseList(inventoryMovements);
    }

    @Override
    public InventoryMovementResponse getInventoryMovementById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }
        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Movimiento de inventario no encontrado con el id: %d", id)));
        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }

    @Override
    public InventoryMovementResponse createInventoryMovement(
            CreateInventoryMovementRequest createInventoryMovementRequest) throws Exception {
        if (Objects.isNull(createInventoryMovementRequest)) {
            throw new Exception("El objeto createInventoryMovementRequest no puede ser nulo.");
        }
        if (createInventoryMovementRequest.getProductId() == null ||
                createInventoryMovementRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }
        if (createInventoryMovementRequest.getOrderId() == null ||
                createInventoryMovementRequest.getOrderId() <= 0) {
            throw new Exception("El campo orderId debe contener un valor mayor a 0.");
        }
        if (createInventoryMovementRequest.getQty() == null ||
                createInventoryMovementRequest.getQty() <= 0) {
            throw new Exception("El campo qty debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(createInventoryMovementRequest.getType()) ||
                createInventoryMovementRequest.getType().isBlank()) {
            throw new Exception("El campo type no puede ser nulo ni vacío.");
        }

        // Validar que el type sea un valor válido del enum
        try {
            InventoryMovementType.valueOf(createInventoryMovementRequest.getType());
        } catch (IllegalArgumentException e) {
            throw new Exception("El campo type contiene un valor no válido.");
        }

        Product product = productRepository.findById(createInventoryMovementRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        Order order = orderRepository.findById(createInventoryMovementRequest.getOrderId())
                .orElseThrow(() -> new Exception("La orden no existe."));

        InventoryMovement inventoryMovement = InventoryMovementMapper
                .createInventoryMovementRequestToInventoryMovement(
                        createInventoryMovementRequest, product, order);
        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }

    @Override
    public InventoryMovementResponse updateInventoryMovement(Integer id, UpdateInventoryMovementRequest updateInventoryMovementRequest) throws Exception {
        // Validar id
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para actualizar");
        }

        // Validar que el movimiento existe
        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Movimiento de inventario no encontrado con el id: %d", id)));

        // Validar campos del request
        if (Objects.isNull(updateInventoryMovementRequest)) {
            throw new Exception("El objeto createInventoryMovementRequest no puede ser nulo.");
        }
        if (updateInventoryMovementRequest.getProductId() == null ||
                updateInventoryMovementRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }
        if (updateInventoryMovementRequest.getOrderId() == null ||
                updateInventoryMovementRequest.getOrderId() <= 0) {
            throw new Exception("El campo orderId debe contener un valor mayor a 0.");
        }
        if (updateInventoryMovementRequest.getQty() == null ||
                updateInventoryMovementRequest.getQty() <= 0) {
            throw new Exception("El campo qty debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(updateInventoryMovementRequest.getType()) ||
                updateInventoryMovementRequest.getType().isBlank()) {
            throw new Exception("El campo type no puede ser nulo ni vacío.");
        }

        // Validar que el type sea un valor válido del enum
        try {
            InventoryMovementType.valueOf(updateInventoryMovementRequest.getType());
        } catch (IllegalArgumentException e) {
            throw new Exception("El campo type contiene un valor no válido.");
        }

        // Validar que el producto existe
        Product product = productRepository.findById(updateInventoryMovementRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        // Validar que la orden existe
        Order order = orderRepository.findById(updateInventoryMovementRequest.getOrderId())
                .orElseThrow(() -> new Exception("La orden no existe."));

        // Actualizar campos
        inventoryMovement.setProduct(product);
        inventoryMovement.setOrder(order);
        inventoryMovement.setQty(updateInventoryMovementRequest.getQty());
        inventoryMovement.setType(InventoryMovementType.valueOf(updateInventoryMovementRequest.getType()));

        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }

    @Override
    public DeleteInventoryMovementResponse deleteInventoryMovement(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para eliminar");
        }

        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Movimiento de inventario no encontrado con el id: %d", id)));

        inventoryMovementRepository.delete(inventoryMovement);

        return DeleteInventoryMovementResponse.builder()
                .message(String.format("Movimiento de inventario con id %d eliminado correctamente", id))
                .build();
    }
}