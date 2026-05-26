package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CartResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteCartResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateCartRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
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
        try {
            List<Cart> carts = cartRepository.findAll();
            if (carts.isEmpty()) {
                return List.of();
            }
            return CartMapper.modelToCartResponseList(carts);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener los carritos: " + e.getMessage());
        }
    }

    @Override
    public CartResponse getCartById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Carrito no encontrado con el id: %d", id)));
        return CartMapper.modelToCartResponse(cart);
    }

    @Override
    public CartResponse createCart(CreateCartRequest createCartRequest) {
        if (Objects.isNull(createCartRequest)) {
            throw new BadRequestException("El objeto createCartRequest no puede ser nulo.");
        }
        if (Objects.isNull(createCartRequest.getStatus()) ||
                createCartRequest.getStatus().isBlank()) {
            throw new BadRequestException("El campo status no puede ser nulo ni vacío.");
        }
        if (createCartRequest.getUserId() == null || createCartRequest.getUserId() <= 0) {
            throw new BadRequestException("El campo userId debe contener un valor mayor a 0.");
        }

        User user = userRepository.findById(createCartRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe."));

        Cart cart = CartMapper.createCartRequestToCart(createCartRequest, user);
        cart = cartRepository.save(cart);
        return CartMapper.modelToCartResponse(cart);
    }

    @Override
    public CartResponse updateCart(Integer id, UpdateCartRequest updateCartRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Carrito no encontrado con el id: %d", id)));
        if (Objects.isNull(updateCartRequest)) {
            throw new BadRequestException("El objeto updateCartRequest no puede ser nulo.");
        }
        if (Objects.isNull(updateCartRequest.getStatus()) ||
                updateCartRequest.getStatus().isBlank()) {
            throw new BadRequestException("El campo status no puede ser nulo ni vacío.");
        }
        if (updateCartRequest.getUserId() == null || updateCartRequest.getUserId() <= 0) {
            throw new BadRequestException("El campo userId debe contener un valor mayor a 0.");
        }

        User user = userRepository.findById(updateCartRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe."));

        cart.setStatus(updateCartRequest.getStatus());
        cart.setUser(user);
        cart.setUpdatedAt(OffsetDateTime.now());

        cart = cartRepository.save(cart);
        return CartMapper.modelToCartResponse(cart);
    }

    @Override
    public DeleteCartResponse deleteCart(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Carrito no encontrado con el id: %d", id)));

        if (cartItemRepository.existsByCartId(id)) {
            throw new BadRequestException("No se puede eliminar el carrito porque tiene items asociados.");
        }

        cartRepository.delete(cart);
        return DeleteCartResponse.builder()
                .message(String.format("Carrito con id %d eliminado correctamente", id))
                .build();
    }
}