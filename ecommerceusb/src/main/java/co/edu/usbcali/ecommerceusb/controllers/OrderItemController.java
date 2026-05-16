package co.edu.usbcali.ecommerceusb.controllers;

import co.edu.usbcali.ecommerceusb.dto.CreateOrderItemRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteOrderItemResponse;
import co.edu.usbcali.ecommerceusb.dto.OrderItemResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateOrderItemRequest;
import co.edu.usbcali.ecommerceusb.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-item")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/all")
    public List<OrderItemResponse> getAll() {
        return orderItemService.getOrderItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItemById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(orderItemService.getOrderItemById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderItemResponse> createOrderItem(
            @RequestBody CreateOrderItemRequest createOrderItemRequest) throws Exception {
        return new ResponseEntity<>(orderItemService.createOrderItem(createOrderItemRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponse> updateOrderItem(
            @PathVariable Integer id,
            @RequestBody UpdateOrderItemRequest updateOrderItemRequest) throws Exception {
        return new ResponseEntity<>(orderItemService.updateOrderItem(id, updateOrderItemRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteOrderItemResponse> deleteOrderItem(
            @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(orderItemService.deleteOrderItem(id), HttpStatus.OK);
    }
}