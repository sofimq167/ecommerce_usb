package co.edu.usbcali.ecommerceusb.service;

import co.edu.usbcali.ecommerceusb.dto.CreateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.ProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateProductCategoryRequest;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategoryResponse> getProductCategories();
    ProductCategoryResponse getProductCategoryById(Integer id) throws Exception;
    ProductCategoryResponse createProductCategory(
            CreateProductCategoryRequest createProductCategoryRequest) throws Exception;
    ProductCategoryResponse updateProductCategory(Integer id, UpdateProductCategoryRequest updateProductCategoryRequest) throws Exception;
    DeleteProductCategoryResponse deleteProductCategory(Integer id) throws Exception;
}