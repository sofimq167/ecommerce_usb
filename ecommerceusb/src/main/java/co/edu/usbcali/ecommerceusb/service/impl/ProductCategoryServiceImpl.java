package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.ProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.mapper.ProductCategoryMapper;
import co.edu.usbcali.ecommerceusb.model.Category;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.model.ProductCategory;
import co.edu.usbcali.ecommerceusb.repository.CategoryRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductCategoryRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductCategoryResponse> getProductCategories() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        if (productCategories.isEmpty()) {
            return List.of();
        }
        return ProductCategoryMapper.modelToProductCategoryResponseList(productCategories);
    }

    @Override
    public ProductCategoryResponse getProductCategoryById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("ProductCategory no encontrado con el id: %d", id)));
        return ProductCategoryMapper.modelToProductCategoryResponse(productCategory);
    }

    @Override
    public ProductCategoryResponse createProductCategory(
            CreateProductCategoryRequest createProductCategoryRequest) throws Exception {
        if (Objects.isNull(createProductCategoryRequest)) {
            throw new Exception("El objeto createProductCategoryRequest no puede ser nulo.");
        }
        if (createProductCategoryRequest.getProductId() == null ||
                createProductCategoryRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }
        if (createProductCategoryRequest.getCategoryId() == null ||
                createProductCategoryRequest.getCategoryId() <= 0) {
            throw new Exception("El campo categoryId debe contener un valor mayor a 0.");
        }

        Product product = productRepository.findById(createProductCategoryRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        Category category = categoryRepository.findById(createProductCategoryRequest.getCategoryId())
                .orElseThrow(() -> new Exception("La categoría no existe."));

        // Validar que no exista ya esa combinación
        if (productCategoryRepository.existsByProductIdAndCategoryId(
                createProductCategoryRequest.getProductId(),
                createProductCategoryRequest.getCategoryId())) {
            throw new Exception("Ya existe esa combinación de producto y categoría.");
        }

        ProductCategory productCategory = ProductCategoryMapper
                .createProductCategoryRequestToProductCategory(
                        createProductCategoryRequest, product, category);
        productCategory = productCategoryRepository.save(productCategory);
        return ProductCategoryMapper.modelToProductCategoryResponse(productCategory);
    }

    @Override
    public ProductCategoryResponse updateProductCategory(Integer id, UpdateProductCategoryRequest updateProductCategoryRequest) throws Exception {
        // Validar id
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para actualizar");
        }

        // Validar que el productCategory existe
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("ProductCategory no encontrado con el id: %d", id)));

        // Validar campos del request
        if (Objects.isNull(updateProductCategoryRequest)) {
            throw new Exception("El objeto createProductCategoryRequest no puede ser nulo.");
        }
        if (updateProductCategoryRequest.getProductId() == null ||
                updateProductCategoryRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }
        if (updateProductCategoryRequest.getCategoryId() == null ||
                updateProductCategoryRequest.getCategoryId() <= 0) {
            throw new Exception("El campo categoryId debe contener un valor mayor a 0.");
        }

        // Validar que el producto existe
        Product product = productRepository.findById(updateProductCategoryRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        // Validar que la categoría existe
        Category category = categoryRepository.findById(updateProductCategoryRequest.getCategoryId())
                .orElseThrow(() -> new Exception("La categoría no existe."));

        // Validar que la combinación no exista en otro productCategory diferente al que estamos actualizando
        if (productCategoryRepository.existsByProductIdAndCategoryId(
                updateProductCategoryRequest.getProductId(),
                updateProductCategoryRequest.getCategoryId()) &&
                (!productCategory.getProduct().getId().equals(updateProductCategoryRequest.getProductId()) ||
                        !productCategory.getCategory().getId().equals(updateProductCategoryRequest.getCategoryId()))) {
            throw new Exception("Ya existe esa combinación de producto y categoría.");
        }

        // Actualizar campos
        productCategory.setProduct(product);
        productCategory.setCategory(category);

        productCategory = productCategoryRepository.save(productCategory);
        return ProductCategoryMapper.modelToProductCategoryResponse(productCategory);
    }

    @Override
    public DeleteProductCategoryResponse deleteProductCategory(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para eliminar");
        }

        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("ProductCategory no encontrado con el id: %d", id)));

        productCategoryRepository.delete(productCategory);

        return DeleteProductCategoryResponse.builder()
                .message(String.format("ProductCategory con id %d eliminado correctamente", id))
                .build();
    }
}