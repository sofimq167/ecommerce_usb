package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CartResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteCartResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateCartRequest;
import co.edu.usbcali.ecommerceusb.mapper.CartMapper;
import co.edu.usbcali.ecommerceusb.model.Cart;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.repository.CartItemRepository;
import co.edu.usbcali.ecommerceusb.repository.CartRepository;
import co.edu.usbcali.ecommerceusb.repository.UserRepository;
import co.edu.usbcali.ecommerceusb.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

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

    @Override
    public CartResponse updateCart(Integer id, UpdateCartRequest updateCartRequest) throws Exception {
        // Validar id
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para actualizar");
        }

        // Validar que el carrito existe
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Carrito no encontrado con el id: %d", id)));

        // Validar campos del request
        if (Objects.isNull(updateCartRequest)) {
            throw new Exception("El objeto createCartRequest no puede ser nulo.");
        }
        if (Objects.isNull(updateCartRequest.getStatus()) ||
                updateCartRequest.getStatus().isBlank()) {
            throw new Exception("El campo status no puede ser nulo ni vacío.");
        }
        if (updateCartRequest.getUserId() == null || updateCartRequest.getUserId() <= 0) {
            throw new Exception("El campo userId debe contener un valor mayor a 0.");
        }

        // Validar que el usuario existe
        User user = userRepository.findById(updateCartRequest.getUserId())
                .orElseThrow(() -> new Exception("El usuario no existe."));

        // Actualizar campos
        cart.setStatus(updateCartRequest.getStatus());
        cart.setUser(user);
        cart.setUpdatedAt(OffsetDateTime.now());

        cart = cartRepository.save(cart);
        return CartMapper.modelToCartResponse(cart);
    }

    @Override
    public DeleteCartResponse deleteCart(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para eliminar");
        }

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Carrito no encontrado con el id: %d", id)));

        // Validar que el carrito no tenga cartItems asociados
        if (cartItemRepository.existsByCartId(id)) {
            throw new Exception("No se puede eliminar el carrito porque tiene items asociados.");
        }

        cartRepository.delete(cart);

        return DeleteCartResponse.builder()
                .message(String.format("Carrito con id %d eliminado correctamente", id))
                .build();
    }
}