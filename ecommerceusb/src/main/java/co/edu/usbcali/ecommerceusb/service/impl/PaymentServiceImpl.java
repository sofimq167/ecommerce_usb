package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreatePaymentRequest;
import co.edu.usbcali.ecommerceusb.dto.DeletePaymentResponse;
import co.edu.usbcali.ecommerceusb.dto.PaymentResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdatePaymentRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.PaymentMapper;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.Payment;
import co.edu.usbcali.ecommerceusb.model.PaymentStatus;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.PaymentRepository;
import co.edu.usbcali.ecommerceusb.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<PaymentResponse> getPayments() {
        try {
            List<Payment> payments = paymentRepository.findAll();
            if (payments.isEmpty()) {
                return List.of();
            }
            return PaymentMapper.modelToPaymentResponseList(payments);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener los pagos: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse getPaymentById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Pago no encontrado con el id: %d", id)));
        return PaymentMapper.modelToPaymentResponse(payment);
    }

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest createPaymentRequest) {
        if (Objects.isNull(createPaymentRequest)) {
            throw new BadRequestException("El objeto createPaymentRequest no puede ser nulo.");
        }
        if (createPaymentRequest.getOrderId() == null ||
                createPaymentRequest.getOrderId() <= 0) {
            throw new BadRequestException("El campo orderId debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(createPaymentRequest.getStatus()) ||
                createPaymentRequest.getStatus().isBlank()) {
            throw new BadRequestException("El campo status no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createPaymentRequest.getIdempotencyKey()) ||
                createPaymentRequest.getIdempotencyKey().isBlank()) {
            throw new BadRequestException("El campo idempotencyKey no puede ser nulo ni vacío.");
        }
        try {
            PaymentStatus.valueOf(createPaymentRequest.getStatus());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El campo status contiene un valor no válido.");
        }

        Order order = orderRepository.findById(createPaymentRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("La orden no existe."));

        if (paymentRepository.existsByIdempotencyKey(createPaymentRequest.getIdempotencyKey())) {
            throw new BadRequestException("Ya existe un pago con el idempotencyKey ingresado.");
        }

        Payment payment = PaymentMapper.createPaymentRequestToPayment(createPaymentRequest, order);
        payment = paymentRepository.save(payment);
        return PaymentMapper.modelToPaymentResponse(payment);
    }

    @Override
    public PaymentResponse updatePayment(Integer id, UpdatePaymentRequest updatePaymentRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Pago no encontrado con el id: %d", id)));
        if (Objects.isNull(updatePaymentRequest)) {
            throw new BadRequestException("El objeto updatePaymentRequest no puede ser nulo.");
        }
        if (updatePaymentRequest.getOrderId() == null ||
                updatePaymentRequest.getOrderId() <= 0) {
            throw new BadRequestException("El campo orderId debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(updatePaymentRequest.getStatus()) ||
                updatePaymentRequest.getStatus().isBlank()) {
            throw new BadRequestException("El campo status no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(updatePaymentRequest.getIdempotencyKey()) ||
                updatePaymentRequest.getIdempotencyKey().isBlank()) {
            throw new BadRequestException("El campo idempotencyKey no puede ser nulo ni vacío.");
        }
        try {
            PaymentStatus.valueOf(updatePaymentRequest.getStatus());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El campo status contiene un valor no válido.");
        }

        Order order = orderRepository.findById(updatePaymentRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("La orden no existe."));

        if (paymentRepository.existsByIdempotencyKey(updatePaymentRequest.getIdempotencyKey()) &&
                !payment.getIdempotencyKey().equals(updatePaymentRequest.getIdempotencyKey())) {
            throw new BadRequestException("Ya existe un pago con el idempotencyKey ingresado.");
        }

        payment.setOrder(order);
        payment.setStatus(PaymentStatus.valueOf(updatePaymentRequest.getStatus()));
        payment.setProviderRef(updatePaymentRequest.getProviderRef());
        payment.setIdempotencyKey(updatePaymentRequest.getIdempotencyKey());

        payment = paymentRepository.save(payment);
        return PaymentMapper.modelToPaymentResponse(payment);
    }

    @Override
    public DeletePaymentResponse deletePayment(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Pago no encontrado con el id: %d", id)));

        paymentRepository.delete(payment);
        return DeletePaymentResponse.builder()
                .message(String.format("Pago con id %d eliminado correctamente", id))
                .build();
    }
}