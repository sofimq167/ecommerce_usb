package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.CartResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartRequest;
import co.edu.usbcali.ecommerceusb.model.Cart;
import co.edu.usbcali.ecommerceusb.model.User;

import java.time.OffsetDateTime;
import java.util.List;

public class CartMapper {

    public static CartResponse modelToCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .status(cart.getStatus())
                .userId(cart.getUser() != null ? cart.getUser().getId() : null)
                .userFullName(cart.getUser() != null ? cart.getUser().getFullName() : null)
                .build();
    }

    public static List<CartResponse> modelToCartResponseList(List<Cart> carts) {
        return carts.stream().map(CartMapper::modelToCartResponse).toList();
    }

    public static Cart createCartRequestToCart(CreateCartRequest request, User user) {
        return Cart.builder()
                .status(request.getStatus())
                .user(user)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}