package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreatePaymentRequest;
import co.edu.usbcali.ecommerceusb.dto.PaymentResponse;
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
        List<Payment> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            return List.of();
        }
        return PaymentMapper.modelToPaymentResponseList(payments);
    }

    @Override
    public PaymentResponse getPaymentById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Pago no encontrado con el id: %d", id)));
        return PaymentMapper.modelToPaymentResponse(payment);
    }

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest createPaymentRequest) throws Exception {
        if (Objects.isNull(createPaymentRequest)) {
            throw new Exception("El objeto createPaymentRequest no puede ser nulo.");
        }
        if (createPaymentRequest.getOrderId() == null || createPaymentRequest.getOrderId() <= 0) {
            throw new Exception("El campo orderId debe contener un valor mayor a 0.");
        }
        if (Objects.isNull(createPaymentRequest.getStatus()) ||
                createPaymentRequest.getStatus().isBlank()) {
            throw new Exception("El campo status no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createPaymentRequest.getIdempotencyKey()) ||
                createPaymentRequest.getIdempotencyKey().isBlank()) {
            throw new Exception("El campo idempotencyKey no puede ser nulo ni vacío.");
        }

        // Validar que el status sea un valor válido del enum
        try {
            PaymentStatus.valueOf(createPaymentRequest.getStatus());
        } catch (IllegalArgumentException e) {
            throw new Exception("El campo status contiene un valor no válido.");
        }

        Order order = orderRepository.findById(createPaymentRequest.getOrderId())
                .orElseThrow(() -> new Exception("La orden no existe."));

        // Validar que no exista ya un pago con el mismo idempotencyKey
        if (paymentRepository.existsByIdempotencyKey(createPaymentRequest.getIdempotencyKey())) {
            throw new Exception("Ya existe un pago con el idempotencyKey ingresado.");
        }

        Payment payment = PaymentMapper.createPaymentRequestToPayment(createPaymentRequest, order);
        payment = paymentRepository.save(payment);
        return PaymentMapper.modelToPaymentResponse(payment);
    }
}