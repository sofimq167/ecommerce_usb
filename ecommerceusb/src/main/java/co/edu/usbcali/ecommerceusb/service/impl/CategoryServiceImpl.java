package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateCategoryRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.CategoryMapper;
import co.edu.usbcali.ecommerceusb.model.Category;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.CategoryRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductCategoryRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<CategoryResponse> getCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                return List.of();
            }
            return CategoryMapper.modelToCategoryResponseList(categories);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener las categorías: " + e.getMessage());
        }
    }

    @Override
    public CategoryResponse getCategoryById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Categoría no encontrada con el id: %d", id)));
        return CategoryMapper.modelToCategoryResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        if (Objects.isNull(createCategoryRequest)) {
            throw new BadRequestException("El objeto createCategoryRequest no puede ser nulo.");
        }
        if (Objects.isNull(createCategoryRequest.getName()) ||
                createCategoryRequest.getName().isBlank()) {
            throw new BadRequestException("El campo name no puede ser nulo ni vacío.");
        }
        if (createCategoryRequest.getProductId() == null ||
                createCategoryRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }

        Product product = productRepository.findById(createCategoryRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        Category category = CategoryMapper.createCategoryRequestToCategory(
                createCategoryRequest, product);
        category = categoryRepository.save(category);
        return CategoryMapper.modelToCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Categoría no encontrada con el id: %d", id)));
        if (Objects.isNull(updateCategoryRequest)) {
            throw new BadRequestException("El objeto updateCategoryRequest no puede ser nulo.");
        }
        if (Objects.isNull(updateCategoryRequest.getName()) ||
                updateCategoryRequest.getName().isBlank()) {
            throw new BadRequestException("El campo name no puede ser nulo ni vacío.");
        }
        if (updateCategoryRequest.getProductId() == null ||
                updateCategoryRequest.getProductId() <= 0) {
            throw new BadRequestException("El campo productId debe contener un valor mayor a 0.");
        }

        Product product = productRepository.findById(updateCategoryRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto no existe."));

        category.setName(updateCategoryRequest.getName());
        category.setProduct(product);

        category = categoryRepository.save(category);
        return CategoryMapper.modelToCategoryResponse(category);
    }

    @Override
    public DeleteCategoryResponse deleteCategory(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Categoría no encontrada con el id: %d", id)));

        if (productCategoryRepository.existsByCategoryId(id)) {
            throw new BadRequestException("No se puede eliminar la categoría porque tiene productos asociados.");
        }

        categoryRepository.delete(category);
        return DeleteCategoryResponse.builder()
                .message(String.format("Categoría con id %d eliminada correctamente", id))
                .build();
    }
}