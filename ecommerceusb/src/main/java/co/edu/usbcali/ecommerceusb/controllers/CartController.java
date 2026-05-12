package co.edu.usbcali.ecommerceusb.controllers;

import co.edu.usbcali.ecommerceusb.dto.CartResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartRequest;
import co.edu.usbcali.ecommerceusb.dto.UpdateCartRequest;
import co.edu.usbcali.ecommerceusb.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/all")
    public List<CartResponse> getAll() {
        return cartService.getCarts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(cartService.getCartById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CartResponse> createCart(
            @RequestBody CreateCartRequest createCartRequest) throws Exception {
        return new ResponseEntity<>(cartService.createCart(createCartRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponse> updateCart(
            @PathVariable Integer id,
            @RequestBody UpdateCartRequest updateCartRequest) throws Exception {
        return new ResponseEntity<>(cartService.updateCart(id, updateCartRequest), HttpStatus.OK);
    }
}