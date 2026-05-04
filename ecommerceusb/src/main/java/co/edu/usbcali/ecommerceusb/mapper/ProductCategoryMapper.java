package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.CreateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.ProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.model.Category;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.model.ProductCategory;

import java.util.List;

public class ProductCategoryMapper {

    public static ProductCategoryResponse modelToProductCategoryResponse(ProductCategory productCategory) {
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .productId(productCategory.getProduct() != null ? productCategory.getProduct().getId() : null)
                .productName(productCategory.getProduct() != null ? productCategory.getProduct().getName() : null)
                .categoryId(productCategory.getCategory() != null ? productCategory.getCategory().getId() : null)
                .categoryName(productCategory.getCategory() != null ? productCategory.getCategory().getName() : null)
                .build();
    }

    public static List<ProductCategoryResponse> modelToProductCategoryResponseList(
            List<ProductCategory> productCategories) {
        return productCategories.stream()
                .map(ProductCategoryMapper::modelToProductCategoryResponse)
                .toList();
    }

    public static ProductCategory createProductCategoryRequestToProductCategory(
            CreateProductCategoryRequest request, Product product, Category category) {
        return ProductCategory.builder()
                .product(product)
                .category(category)
                .build();
    }
}