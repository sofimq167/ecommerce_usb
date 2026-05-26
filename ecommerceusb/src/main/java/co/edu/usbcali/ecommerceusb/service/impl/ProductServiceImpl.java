package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateProductRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteProductResponse;
import co.edu.usbcali.ecommerceusb.dto.ProductResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateProductRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.ProductMapper;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.*;
import co.edu.usbcali.ecommerceusb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Override
    public List<ProductResponse> getProducts() {
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                return List.of();
            }
            return ProductMapper.modelToProductResponseList(products);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener los productos: " + e.getMessage());
        }
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Producto no encontrado con el id: %d", id)));
        return ProductMapper.modelToProductResponse(product);
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        if (Objects.isNull(createProductRequest)) {
            throw new BadRequestException("El objeto createProductRequest no puede ser nulo.");
        }
        if (Objects.isNull(createProductRequest.getName()) ||
                createProductRequest.getName().isBlank()) {
            throw new BadRequestException("El campo name no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createProductRequest.getPrice())) {
            throw new BadRequestException("El campo price no puede ser nulo.");
        }
        if (createProductRequest.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El campo price debe ser mayor a 0.");
        }
        if (Objects.isNull(createProductRequest.getAvailable())) {
            throw new BadRequestException("El campo available no puede ser nulo.");
        }

        Product product = ProductMapper.createProductRequestToProduct(createProductRequest);
        product = productRepository.save(product);
        return ProductMapper.modelToProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Integer id, UpdateProductRequest updateProductRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Producto no encontrado con el id: %d", id)));
        if (Objects.isNull(updateProductRequest)) {
            throw new BadRequestException("El objeto updateProductRequest no puede ser nulo.");
        }
        if (Objects.isNull(updateProductRequest.getName()) ||
                updateProductRequest.getName().isBlank()) {
            throw new BadRequestException("El campo name no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(updateProductRequest.getPrice())) {
            throw new BadRequestException("El campo price no puede ser nulo.");
        }
        if (updateProductRequest.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El campo price debe ser mayor a 0.");
        }
        if (Objects.isNull(updateProductRequest.getAvailable())) {
            throw new BadRequestException("El campo available no puede ser nulo.");
        }

        product.setName(updateProductRequest.getName());
        product.setDescription(updateProductRequest.getDescription());
        product.setPrice(updateProductRequest.getPrice());
        product.setAvailable(updateProductRequest.getAvailable());
        product.setUpdatedAt(OffsetDateTime.now());

        product = productRepository.save(product);
        return ProductMapper.modelToProductResponse(product);
    }

    @Override
    public DeleteProductResponse deleteProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Producto no encontrado con el id: %d", id)));

        if (cartItemRepository.existsByProductId(id)) {
            throw new BadRequestException("No se puede eliminar el producto porque tiene items de carrito asociados.");
        }
        if (orderItemRepository.existsByProductId(id)) {
            throw new BadRequestException("No se puede eliminar el producto porque tiene items de orden asociados.");
        }
        if (inventoryRepository.existsByProductId(id)) {
            throw new BadRequestException("No se puede eliminar el producto porque tiene inventario asociado.");
        }
        if (productCategoryRepository.existsByProductId(id)) {
            throw new BadRequestException("No se puede eliminar el producto porque tiene categorías asociadas.");
        }
        if (inventoryMovementRepository.existsByProductId(id)) {
            throw new BadRequestException("No se puede eliminar el producto porque tiene movimientos de inventario asociados.");
        }

        productRepository.delete(product);
        return DeleteProductResponse.builder()
                .message(String.format("Producto con id %d eliminado correctamente", id))
                .build();
    }
}