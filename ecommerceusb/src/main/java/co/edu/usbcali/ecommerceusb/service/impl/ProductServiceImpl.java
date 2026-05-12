package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateProductRequest;
import co.edu.usbcali.ecommerceusb.dto.ProductResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateProductRequest;
import co.edu.usbcali.ecommerceusb.mapper.ProductMapper;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
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

    @Override
    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return List.of();
        }
        return ProductMapper.modelToProductResponseList(products);
    }

    @Override
    public ProductResponse getProductById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Producto no encontrado con el id: %d", id)));
        return ProductMapper.modelToProductResponse(product);
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest createProductRequest) throws Exception {
        if (Objects.isNull(createProductRequest)) {
            throw new Exception("El objeto createProductRequest no puede ser nulo.");
        }
        if (Objects.isNull(createProductRequest.getName()) ||
                createProductRequest.getName().isBlank()) {
            throw new Exception("El campo name no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createProductRequest.getPrice())) {
            throw new Exception("El campo price no puede ser nulo.");
        }
        if (createProductRequest.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new Exception("El campo price debe ser mayor a 0.");
        }
        if (Objects.isNull(createProductRequest.getAvailable())) {
            throw new Exception("El campo available no puede ser nulo.");
        }

        Product product = ProductMapper.createProductRequestToProduct(createProductRequest);
        product = productRepository.save(product);
        return ProductMapper.modelToProductResponse(product);
    }

    // PUT
    @Override
    public ProductResponse updateProduct(Integer id, UpdateProductRequest updateProductRequest) throws Exception {
        // Validar id
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para actualizar");
        }

        // Validar que el producto existe
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Producto no encontrado con el id: %d", id)));

        // Validar campos del request
        if (Objects.isNull(updateProductRequest)) {
            throw new Exception("El objeto createProductRequest no puede ser nulo.");
        }
        if (Objects.isNull(updateProductRequest.getName()) ||
                updateProductRequest.getName().isBlank()) {
            throw new Exception("El campo name no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(updateProductRequest.getPrice())) {
            throw new Exception("El campo price no puede ser nulo.");
        }
        if (updateProductRequest.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new Exception("El campo price debe ser mayor a 0.");
        }
        if (Objects.isNull(updateProductRequest.getAvailable())) {
            throw new Exception("El campo available no puede ser nulo.");
        }

        // Actualizar campos
        product.setName(updateProductRequest.getName());
        product.setDescription(updateProductRequest.getDescription());
        product.setPrice(updateProductRequest.getPrice());
        product.setAvailable(updateProductRequest.getAvailable());
        product.setUpdatedAt(OffsetDateTime.now());

        product = productRepository.save(product);
        return ProductMapper.modelToProductResponse(product);
    }
}