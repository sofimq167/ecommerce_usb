package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.CategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCategoryRequest;
import co.edu.usbcali.ecommerceusb.model.Category;
import co.edu.usbcali.ecommerceusb.model.Product;

import java.time.OffsetDateTime;
import java.util.List;

public class CategoryMapper {

    public static CategoryResponse modelToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .productId(category.getProduct() != null ? category.getProduct().getId() : null)
                .productName(category.getProduct() != null ? category.getProduct().getName() : null)
                .build();
    }

    public static List<CategoryResponse> modelToCategoryResponseList(List<Category> categories) {
        return categories.stream().map(CategoryMapper::modelToCategoryResponse).toList();
    }

    public static Category createCategoryRequestToCategory(CreateCategoryRequest request,
                                                           Product product) {
        return Category.builder()
                .name(request.getName())
                .product(product)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}