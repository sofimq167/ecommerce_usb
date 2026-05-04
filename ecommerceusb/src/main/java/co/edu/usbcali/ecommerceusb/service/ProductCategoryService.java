package co.edu.usbcali.ecommerceusb.service;

import co.edu.usbcali.ecommerceusb.dto.CreateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.ProductCategoryResponse;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategoryResponse> getProductCategories();
    ProductCategoryResponse getProductCategoryById(Integer id) throws Exception;
    ProductCategoryResponse createProductCategory(
            CreateProductCategoryRequest createProductCategoryRequest) throws Exception;
}