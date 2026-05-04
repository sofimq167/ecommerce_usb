package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CartResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartRequest;
import co.edu.usbcali.ecommerceusb.mapper.CartMapper;
import co.edu.usbcali.ecommerceusb.model.Cart;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.repository.CartRepository;
import co.edu.usbcali.ecommerceusb.repository.UserRepository;
import co.edu.usbcali.ecommerceusb.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CartResponse> getCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            return List.of();
        }
        return CartMapper.modelToCartResponseList(carts);
    }

    @Override
    public CartResponse getCartById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Carrito no encontrado con el id: %d", id)));
        return CartMapper.modelToCartResponse(cart);
    }

    @Override
    public CartResponse createCart(CreateCartRequest createCartRequest) throws Exception {
        if (Objects.isNull(createCartRequest)) {
            throw new Exception("El objeto createCartRequest no puede ser nulo.");
        }
        if (Objects.isNull(createCartRequest.getStatus()) || createCartRequest.getStatus().isBlank()) {
            throw new Exception("El campo status no puede ser nulo ni vacío.");
        }
        if (createCartRequest.getUserId() == null || createCartRequest.getUserId() <= 0) {
            throw new Exception("El campo userId debe contener un valor mayor a 0.");
        }

        User user = userRepository.findById(createCartRequest.getUserId())
                .orElseThrow(() -> new Exception("El usuario no existe."));

        Cart cart = CartMapper.createCartRequestToCart(createCartRequest, user);
        cart = cartRepository.save(cart);
        return CartMapper.modelToCartResponse(cart);
    }
}