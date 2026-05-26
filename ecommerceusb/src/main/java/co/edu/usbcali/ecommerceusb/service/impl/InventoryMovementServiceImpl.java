package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateInventoryMovementRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteInventoryMovementResponse;
import co.edu.usbcali.ecommerceusb.dto.InventoryMovementResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateInventoryMovementRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
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
        try {
            List<InventoryMovement> inventoryMovements = inventoryMovementRepository.findAll();
            if (inventoryMovements.isEmpty()) {
                return List.of();
            }
            return InventoryMovementMapper.modelToInventoryMovementResponseList(inventoryMovements);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener los movimientos de inventario: " + e.getMessage());
        }
    }

    @Override
    public InventoryMovementResponse getInventoryMovementById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Movimiento de inventario no encontrado con el id: %d", id)));
        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }

    @Override
    public InventoryMovementResponse createInventoryMovement(
            CreateInventoryMovementRequest createInventoryMovementRequest) {
        if (Objects.isNull(createInventoryMovementRequest)) {
            throw new BadRequestException("El objeto createInventoryMovementRequest no puede ser nulo.");
        }
        if (createInventoryMovementRequest.getProductId() == null ||
                createInventoryMovementRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }
        if (createInventoryMovementRequest.getOrderId() == null ||
                createInventoryMovementRequest.getOrderId() <= 0) {
            throw new BadRequestException("El campo orderId debe contener un valor mayor a 0.");
        }
        if (createInventoryMovementRequest.getQty() == null ||
                createInventoryMovementRequest.getQty() <= 0) {
            throw new BadRequestException("El campo qty debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(createInventoryMovementRequest.getType()) ||
                createInventoryMovementRequest.getType().isBlank()) {
            throw new BadRequestException("El campo type no puede ser nulo ni vacío.");
        }
        try {
            InventoryMovementType.valueOf(createInventoryMovementRequest.getType());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El campo type contiene un valor no válido.");
        }

        Product product = productRepository.findById(createInventoryMovementRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        Order order = orderRepository.findById(createInventoryMovementRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("La orden no existe."));

        InventoryMovement inventoryMovement = InventoryMovementMapper
                .createInventoryMovementRequestToInventoryMovement(
                        createInventoryMovementRequest, product, order);
        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }

    @Override
    public InventoryMovementResponse updateInventoryMovement(Integer id,
                                                             UpdateInventoryMovementRequest updateInventoryMovementRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Movimiento de inventario no encontrado con el id: %d", id)));
        if (Objects.isNull(updateInventoryMovementRequest)) {
            throw new BadRequestException("El objeto updateInventoryMovementRequest no puede ser nulo.");
        }
        if (updateInventoryMovementRequest.getProductId() == null ||
                updateInventoryMovementRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }
        if (updateInventoryMovementRequest.getOrderId() == null ||
                updateInventoryMovementRequest.getOrderId() <= 0) {
            throw new BadRequestException("El campo orderId debe contener un valor mayor a 0.");
        }
        if (updateInventoryMovementRequest.getQty() == null ||
                updateInventoryMovementRequest.getQty() <= 0) {
            throw new BadRequestException("El campo qty debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(updateInventoryMovementRequest.getType()) ||
                updateInventoryMovementRequest.getType().isBlank()) {
            throw new BadRequestException("El campo type no puede ser nulo ni vacío.");
        }
        try {
            InventoryMovementType.valueOf(updateInventoryMovementRequest.getType());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El campo type contiene un valor no válido.");
        }

        Product product = productRepository.findById(updateInventoryMovementRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        Order order = orderRepository.findById(updateInventoryMovementRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("La orden no existe."));

        inventoryMovement.setProduct(product);
        inventoryMovement.setOrder(order);
        inventoryMovement.setQty(updateInventoryMovementRequest.getQty());
        inventoryMovement.setType(InventoryMovementType.valueOf(updateInventoryMovementRequest.getType()));

        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }

    @Override
    public DeleteInventoryMovementResponse deleteInventoryMovement(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Movimiento de inventario no encontrado con el id: %d", id)));

        inventoryMovementRepository.delete(inventoryMovement);
        return DeleteInventoryMovementResponse.builder()
                .message(String.format("Movimiento de inventario con id %d eliminado correctamente", id))
                .build();
    }
}