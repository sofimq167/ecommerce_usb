package co.edu.usbcali.ecommerceusb.controllers;

import co.edu.usbcali.ecommerceusb.dto.CartItemResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartItemRequest;
import co.edu.usbcali.ecommerceusb.dto.UpdateCartItemRequest;
import co.edu.usbcali.ecommerceusb.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-item")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/all")
    public List<CartItemResponse> getAll() {
        return cartItemService.getCartItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponse> getCartItemById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(cartItemService.getCartItemById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> createCartItem(
            @RequestBody CreateCartItemRequest createCartItemRequest) throws Exception {
        return new ResponseEntity<>(cartItemService.createCartItem(createCartItemRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable Integer id,
            @RequestBody UpdateCartItemRequest updateCartItemRequest) throws Exception {
        return new ResponseEntity<>(cartItemService.updateCartItem(id, updateCartItemRequest), HttpStatus.OK);
    }
}