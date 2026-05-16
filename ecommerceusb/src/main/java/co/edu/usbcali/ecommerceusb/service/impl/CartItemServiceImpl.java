package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CartItemResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartItemRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteCartItemResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateCartItemRequest;
import co.edu.usbcali.ecommerceusb.mapper.CartItemMapper;
import co.edu.usbcali.ecommerceusb.model.Cart;
import co.edu.usbcali.ecommerceusb.model.CartItem;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.CartItemRepository;
import co.edu.usbcali.ecommerceusb.repository.CartRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CartItemResponse> getCartItems() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        if (cartItems.isEmpty()) {
            return List.of();
        }
        return CartItemMapper.modelToCartItemResponseList(cartItems);
    }

    @Override
    public CartItemResponse getCartItemById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("CartItem no encontrado con el id: %d", id)));
        return CartItemMapper.modelToCartItemResponse(cartItem);
    }

    @Override
    public CartItemResponse createCartItem(CreateCartItemRequest createCartItemRequest) throws Exception {
        if (Objects.isNull(createCartItemRequest)) {
            throw new Exception("El objeto createCartItemRequest no puede ser nulo.");
        }
        if (createCartItemRequest.getCartId() == null || createCartItemRequest.getCartId() <= 0) {
            throw new Exception("El campo cartId debe contener un valor mayor a 0.");
        }
        if (createCartItemRequest.getProductId() == null || createCartItemRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }
        if (createCartItemRequest.getQuantity() == null || createCartItemRequest.getQuantity() <= 0) {
            throw new Exception("El campo quantity debe contener un valor mayor a 0.");
        }

        Cart cart = cartRepository.findById(createCartItemRequest.getCartId())
                .orElseThrow(() -> new Exception("El carrito no existe."));

        Product product = productRepository.findById(createCartItemRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        CartItem cartItem = CartItemMapper.createCartItemRequestToCartItem(
                createCartItemRequest, cart, product);
        cartItem = cartItemRepository.save(cartItem);
        return CartItemMapper.modelToCartItemResponse(cartItem);
    }

    @Override
    public CartItemResponse updateCartItem(Integer id, UpdateCartItemRequest updateCartItemRequest) throws Exception {
        // Validar id
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para actualizar");
        }

        // Validar que el cartItem existe
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("CartItem no encontrado con el id: %d", id)));

        // Validar campos del request
        if (Objects.isNull(updateCartItemRequest)) {
            throw new Exception("El objeto createCartItemRequest no puede ser nulo.");
        }
        if (updateCartItemRequest.getCartId() == null || updateCartItemRequest.getCartId() <= 0) {
            throw new Exception("El campo cartId debe contener un valor mayor a 0.");
        }
        if (updateCartItemRequest.getProductId() == null || updateCartItemRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }
        if (updateCartItemRequest.getQuantity() == null || updateCartItemRequest.getQuantity() <= 0) {
            throw new Exception("El campo quantity debe contener un valor mayor a 0.");
        }

        // Validar que el carrito existe
        Cart cart = cartRepository.findById(updateCartItemRequest.getCartId())
                .orElseThrow(() -> new Exception("El carrito no existe."));

        // Validar que el producto existe
        Product product = productRepository.findById(updateCartItemRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        // Actualizar campos
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(updateCartItemRequest.getQuantity());
        cartItem.setUpdatedAt(OffsetDateTime.now());

        cartItem = cartItemRepository.save(cartItem);
        return CartItemMapper.modelToCartItemResponse(cartItem);
    }

    @Override
    public DeleteCartItemResponse deleteCartItem(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para eliminar");
        }

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("CartItem no encontrado con el id: %d", id)));

        cartItemRepository.delete(cartItem);

        return DeleteCartItemResponse.builder()
                .message(String.format("CartItem con id %d eliminado correctamente", id))
                .build();
    }
}