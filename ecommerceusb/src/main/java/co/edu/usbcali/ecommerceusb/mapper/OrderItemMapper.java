package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.CreateOrderItemRequest;
import co.edu.usbcali.ecommerceusb.dto.OrderItemResponse;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.OrderItem;
import co.edu.usbcali.ecommerceusb.model.Product;

import java.time.OffsetDateTime;
import java.util.List;

public class OrderItemMapper {

    public static OrderItemResponse modelToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder() != null ? orderItem.getOrder().getId() : null)
                .productId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null)
                .productName(orderItem.getProduct() != null ? orderItem.getProduct().getName() : null)
                .quantity(orderItem.getQuantity())
                .unitPriceSnapshot(orderItem.getUnitPriceSnapshot())
                .lineTotal(orderItem.getLineTotal())
                .build();
    }

    public static List<OrderItemResponse> modelToOrderItemResponseList(List<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItemMapper::modelToOrderItemResponse).toList();
    }

    public static OrderItem createOrderItemRequestToOrderItem(CreateOrderItemRequest request,
                                                              Order order, Product product) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(request.getQuantity())
                .unitPriceSnapshot(request.getUnitPriceSnapshot())
                .lineTotal(request.getLineTotal())
                .createdAt(OffsetDateTime.now())
                .build();
    }
}