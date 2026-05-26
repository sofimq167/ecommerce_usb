package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateOrderItemRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteOrderItemResponse;
import co.edu.usbcali.ecommerceusb.dto.OrderItemResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateOrderItemRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.OrderItemMapper;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.OrderItem;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.OrderItemRepository;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<OrderItemResponse> getOrderItems() {
        try {
            List<OrderItem> orderItems = orderItemRepository.findAll();
            if (orderItems.isEmpty()) {
                return List.of();
            }
            return OrderItemMapper.modelToOrderItemResponseList(orderItems);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener los items de la orden: " + e.getMessage());
        }
    }

    @Override
    public OrderItemResponse getOrderItemById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("OrderItem no encontrado con el id: %d", id)));
        return OrderItemMapper.modelToOrderItemResponse(orderItem);
    }

    @Override
    public OrderItemResponse createOrderItem(CreateOrderItemRequest createOrderItemRequest) {
        if (Objects.isNull(createOrderItemRequest)) {
            throw new BadRequestException("El objeto createOrderItemRequest no puede ser nulo.");
        }
        if (createOrderItemRequest.getOrderId() == null ||
                createOrderItemRequest.getOrderId() <= 0) {
            throw new BadRequestException("El campo orderId debe contener un valor mayor a 0.");
        }
        if (createOrderItemRequest.getProductId() == null ||
                createOrderItemRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }
        if (createOrderItemRequest.getQuantity() == null ||
                createOrderItemRequest.getQuantity() <= 0) {
            throw new BadRequestException("El campo quantity debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(createOrderItemRequest.getUnitPriceSnapshot())) {
            throw new BadRequestException("El campo unitPriceSnapshot no puede ser nulo.");
        }
        if (Objects.isNull(createOrderItemRequest.getLineTotal())) {
            throw new BadRequestException("El campo lineTotal no puede ser nulo.");
        }

        Order order = orderRepository.findById(createOrderItemRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("La orden no existe."));

        Product product = productRepository.findById(createOrderItemRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        if (orderItemRepository.existsByOrderIdAndProductId(
                createOrderItemRequest.getOrderId(), createOrderItemRequest.getProductId())) {
            throw new BadRequestException("Ya existe ese producto en la orden ingresada.");
        }

        OrderItem orderItem = OrderItemMapper.createOrderItemRequestToOrderItem(
                createOrderItemRequest, order, product);
        orderItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.modelToOrderItemResponse(orderItem);
    }

    @Override
    public OrderItemResponse updateOrderItem(Integer id, UpdateOrderItemRequest updateOrderItemRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("OrderItem no encontrado con el id: %d", id)));
        if (Objects.isNull(updateOrderItemRequest)) {
            throw new BadRequestException("El objeto updateOrderItemRequest no puede ser nulo.");
        }
        if (updateOrderItemRequest.getOrderId() == null ||
                updateOrderItemRequest.getOrderId() <= 0) {
            throw new BadRequestException("El campo orderId debe contener un valor mayor a 0.");
        }
        if (updateOrderItemRequest.getProductId() == null ||
                updateOrderItemRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }
        if (updateOrderItemRequest.getQuantity() == null ||
                updateOrderItemRequest.getQuantity() <= 0) {
            throw new BadRequestException("El campo quantity debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(updateOrderItemRequest.getUnitPriceSnapshot())) {
            throw new BadRequestException("El campo unitPriceSnapshot no puede ser nulo.");
        }
        if (Objects.isNull(updateOrderItemRequest.getLineTotal())) {
            throw new BadRequestException("El campo lineTotal no puede ser nulo.");
        }

        Order order = orderRepository.findById(updateOrderItemRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("La orden no existe."));

        Product product = productRepository.findById(updateOrderItemRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        if (orderItemRepository.existsByOrderIdAndProductId(
                updateOrderItemRequest.getOrderId(),
                updateOrderItemRequest.getProductId()) &&
                (!orderItem.getOrder().getId().equals(updateOrderItemRequest.getOrderId()) ||
                        !orderItem.getProduct().getId().equals(updateOrderItemRequest.getProductId()))) {
            throw new BadRequestException("Ya existe ese producto en la orden ingresada.");
        }

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(updateOrderItemRequest.getQuantity());
        orderItem.setUnitPriceSnapshot(updateOrderItemRequest.getUnitPriceSnapshot());
        orderItem.setLineTotal(updateOrderItemRequest.getLineTotal());

        orderItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.modelToOrderItemResponse(orderItem);
    }

    @Override
    public DeleteOrderItemResponse deleteOrderItem(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("OrderItem no encontrado con el id: %d", id)));

        orderItemRepository.delete(orderItem);
        return DeleteOrderItemResponse.builder()
                .message(String.format("OrderItem con id %d eliminado correctamente", id))
                .build();
    }
}