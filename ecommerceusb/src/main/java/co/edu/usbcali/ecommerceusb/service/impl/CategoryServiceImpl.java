package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.UpdateCategoryRequest;
import co.edu.usbcali.ecommerceusb.mapper.CategoryMapper;
import co.edu.usbcali.ecommerceusb.model.Category;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.CategoryRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return List.of();
        }
        return CategoryMapper.modelToCategoryResponseList(categories);
    }

    @Override
    public CategoryResponse getCategoryById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Categoría no encontrada con el id: %d", id)));
        return CategoryMapper.modelToCategoryResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) throws Exception {
        if (Objects.isNull(createCategoryRequest)) {
            throw new Exception("El objeto createCategoryRequest no puede ser nulo.");
        }
        if (Objects.isNull(createCategoryRequest.getName()) || createCategoryRequest.getName().isBlank()) {
            throw new Exception("El campo name no puede ser nulo ni vacío.");
        }
        if (createCategoryRequest.getProductId() == null || createCategoryRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }

        Product product = productRepository.findById(createCategoryRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        Category category = CategoryMapper.createCategoryRequestToCategory(createCategoryRequest, product);
        category = categoryRepository.save(category);
        return CategoryMapper.modelToCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest) throws Exception {
        // Validar id
        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para actualizar");
        }

        // Validar que la categoría existe
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Categoría no encontrada con el id: %d", id)));

        // Validar campos del request
        if (Objects.isNull(updateCategoryRequest)) {
            throw new Exception("El objeto createCategoryRequest no puede ser nulo.");
        }
        if (Objects.isNull(updateCategoryRequest.getName()) ||
                updateCategoryRequest.getName().isBlank()) {
            throw new Exception("El campo name no puede ser nulo ni vacío.");
        }
        if (updateCategoryRequest.getProductId() == null ||
                updateCategoryRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0.");
        }

        // Validar que el producto existe
        Product product = productRepository.findById(updateCategoryRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe."));

        // Actualizar campos
        category.setName(updateCategoryRequest.getName());
        category.setProduct(product);

        category = categoryRepository.save(category);
        return CategoryMapper.modelToCategoryResponse(category);
    }
}