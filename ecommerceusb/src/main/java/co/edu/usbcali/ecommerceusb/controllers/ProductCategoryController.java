package co.edu.usbcali.ecommerceusb.controllers;

import co.edu.usbcali.ecommerceusb.dto.CreateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.ProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/all")
    public List<ProductCategoryResponse> getAll() {
        return productCategoryService.getProductCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResponse> getProductCategoryById(
            @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(
                productCategoryService.getProductCategoryById(id), HttpStatus.OK);
    }
//
    @PostMapping
    public ResponseEntity<ProductCategoryResponse> createProductCategory(
            @RequestBody CreateProductCategoryRequest createProductCategoryRequest) throws Exception {
        return new ResponseEntity<>(
                productCategoryService.createProductCategory(createProductCategoryRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryResponse> updateProductCategory(
            @PathVariable Integer id,
            @RequestBody UpdateProductCategoryRequest updateProductCategoryRequest) throws Exception {
        return new ResponseEntity<>(productCategoryService.updateProductCategory(id, updateProductCategoryRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteProductCategoryResponse> deleteProductCategory(
            @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(productCategoryService.deleteProductCategory(id), HttpStatus.OK);
    }
}