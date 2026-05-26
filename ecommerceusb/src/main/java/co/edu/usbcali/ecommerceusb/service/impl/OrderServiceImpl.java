package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateOrderRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteOrderResponse;
import co.edu.usbcali.ecommerceusb.dto.OrderResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateOrderRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.OrderMapper;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.OrderStatus;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.repository.InventoryMovementRepository;
import co.edu.usbcali.ecommerceusb.repository.OrderItemRepository;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.PaymentRepository;
import co.edu.usbcali.ecommerceusb.repository.UserRepository;
import co.edu.usbcali.ecommerceusb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Override
    public List<OrderResponse> getOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            if (orders.isEmpty()) {
                return List.of();
            }
            return OrderMapper.modelToOrderResponseList(orders);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener las órdenes: " + e.getMessage());
        }
    }

    @Override
    public OrderResponse getOrderById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Orden no encontrada con el id: %d", id)));
        return OrderMapper.modelToOrderResponse(order);
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        if (Objects.isNull(createOrderRequest)) {
            throw new BadRequestException("El objeto createOrderRequest no puede ser nulo.");
        }
        if (createOrderRequest.getUserId() == null || createOrderRequest.getUserId() <= 0) {
            throw new BadRequestException("El campo userId debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(createOrderRequest.getStatus()) ||
                createOrderRequest.getStatus().isBlank()) {
            throw new BadRequestException("El campo status no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createOrderRequest.getTotalAmount())) {
            throw new BadRequestException("El campo totalAmount no puede ser nulo.");
        }
        if (Objects.isNull(createOrderRequest.getCurrency()) ||
                createOrderRequest.getCurrency().isBlank()) {
            throw new BadRequestException("El campo currency no puede ser nulo ni vacío.");
        }
        try {
            OrderStatus.valueOf(createOrderRequest.getStatus());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El campo status contiene un valor no válido.");
        }

        User user = userRepository.findById(createOrderRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe."));

        Order order = OrderMapper.createOrderRequestToOrder(createOrderRequest, user);
        order = orderRepository.save(order);
        return OrderMapper.modelToOrderResponse(order);
    }

    @Override
    public OrderResponse updateOrder(Integer id, UpdateOrderRequest updateOrderRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Orden no encontrada con el id: %d", id)));
        if (Objects.isNull(updateOrderRequest)) {
            throw new BadRequestException("El objeto updateOrderRequest no puede ser nulo.");
        }
        if (updateOrderRequest.getUserId() == null || updateOrderRequest.getUserId() <= 0) {
            throw new BadRequestException("El campo userId debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(updateOrderRequest.getStatus()) ||
                updateOrderRequest.getStatus().isBlank()) {
            throw new BadRequestException("El campo status no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(updateOrderRequest.getTotalAmount())) {
            throw new BadRequestException("El campo totalAmount no puede ser nulo.");
        }
        if (Objects.isNull(updateOrderRequest.getCurrency()) ||
                updateOrderRequest.getCurrency().isBlank()) {
            throw new BadRequestException("El campo currency no puede ser nulo ni vacío.");
        }
        try {
            OrderStatus.valueOf(updateOrderRequest.getStatus());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El campo status contiene un valor no válido.");
        }

        User user = userRepository.findById(updateOrderRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe."));

        order.setUser(user);
        order.setStatus(OrderStatus.valueOf(updateOrderRequest.getStatus()));
        order.setTotalAmount(updateOrderRequest.getTotalAmount());
        order.setCurrency(updateOrderRequest.getCurrency());

        order = orderRepository.save(order);
        return OrderMapper.modelToOrderResponse(order);
    }

    @Override
    public DeleteOrderResponse deleteOrder(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Orden no encontrada con el id: %d", id)));

        if (orderItemRepository.existsByOrderId(id)) {
            throw new BadRequestException("No se puede eliminar la orden porque tiene items asociados.");
        }
        if (paymentRepository.existsByOrderId(id)) {
            throw new BadRequestException("No se puede eliminar la orden porque tiene pagos asociados.");
        }
        if (inventoryMovementRepository.existsByOrderId(id)) {
            throw new BadRequestException("No se puede eliminar la orden porque tiene movimientos de inventario asociados.");
        }

        orderRepository.delete(order);
        return DeleteOrderResponse.builder()
                .message(String.format("Orden con id %d eliminada correctamente", id))
                .build();
    }
}