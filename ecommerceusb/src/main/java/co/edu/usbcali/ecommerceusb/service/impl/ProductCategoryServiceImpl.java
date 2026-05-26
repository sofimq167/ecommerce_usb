package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.ProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
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
        try {
            List<ProductCategory> productCategories = productCategoryRepository.findAll();
            if (productCategories.isEmpty()) {
                return List.of();
            }
            return ProductCategoryMapper.modelToProductCategoryResponseList(productCategories);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener las categorías de productos: " + e.getMessage());
        }
    }

    @Override
    public ProductCategoryResponse getProductCategoryById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("ProductCategory no encontrado con el id: %d", id)));
        return ProductCategoryMapper.modelToProductCategoryResponse(productCategory);
    }

    @Override
    public ProductCategoryResponse createProductCategory(
            CreateProductCategoryRequest createProductCategoryRequest) {
        if (Objects.isNull(createProductCategoryRequest)) {
            throw new BadRequestException("El objeto createProductCategoryRequest no puede ser nulo.");
        }
        if (createProductCategoryRequest.getProductId() == null ||
                createProductCategoryRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }
        if (createProductCategoryRequest.getCategoryId() == null ||
                createProductCategoryRequest.getCategoryId() <= 0) {
            throw new BadRequestException("El campo categoryId debe contener un valor mayor a 0.");
        }

        Product product = productRepository.findById(createProductCategoryRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        Category category = categoryRepository.findById(createProductCategoryRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("La categoría no existe."));

        if (productCategoryRepository.existsByProductIdAndCategoryId(
                createProductCategoryRequest.getProductId(),
                createProductCategoryRequest.getCategoryId())) {
            throw new BadRequestException("Ya existe esa combinación de producto y categoría.");
        }

        ProductCategory productCategory = ProductCategoryMapper
                .createProductCategoryRequestToProductCategory(
                        createProductCategoryRequest, product, category);
        productCategory = productCategoryRepository.save(productCategory);
        return ProductCategoryMapper.modelToProductCategoryResponse(productCategory);
    }

    @Override
    public ProductCategoryResponse updateProductCategory(Integer id,
                                                         UpdateProductCategoryRequest updateProductCategoryRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("ProductCategory no encontrado con el id: %d", id)));
        if (Objects.isNull(updateProductCategoryRequest)) {
            throw new BadRequestException("El objeto updateProductCategoryRequest no puede ser nulo.");
        }
        if (updateProductCategoryRequest.getProductId() == null ||
                updateProductCategoryRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }
        if (updateProductCategoryRequest.getCategoryId() == null ||
                updateProductCategoryRequest.getCategoryId() <= 0) {
            throw new BadRequestException("El campo categoryId debe contener un valor mayor a 0.");
        }

        Product product = productRepository.findById(updateProductCategoryRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        Category category = categoryRepository.findById(updateProductCategoryRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("La categoría no existe."));

        if (productCategoryRepository.existsByProductIdAndCategoryId(
                updateProductCategoryRequest.getProductId(),
                updateProductCategoryRequest.getCategoryId()) &&
                (!productCategory.getProduct().getId().equals(updateProductCategoryRequest.getProductId()) ||
                        !productCategory.getCategory().getId().equals(updateProductCategoryRequest.getCategoryId()))) {
            throw new BadRequestException("Ya existe esa combinación de producto y categoría.");
        }

        productCategory.setProduct(product);
        productCategory.setCategory(category);

        productCategory = productCategoryRepository.save(productCategory);
        return ProductCategoryMapper.modelToProductCategoryResponse(productCategory);
    }

    @Override
    public DeleteProductCategoryResponse deleteProductCategory(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("ProductCategory no encontrado con el id: %d", id)));

        productCategoryRepository.delete(productCategory);
        return DeleteProductCategoryResponse.builder()
                .message(String.format("ProductCategory con id %d eliminado correctamente", id))
                .build();
    }
}