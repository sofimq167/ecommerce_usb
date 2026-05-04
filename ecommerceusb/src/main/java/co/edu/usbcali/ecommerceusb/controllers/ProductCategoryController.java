package co.edu.usbcali.ecommerceusb.controllers;

import co.edu.usbcali.ecommerceusb.dto.CreateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.ProductCategoryResponse;
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

    @PostMapping
    public ResponseEntity<ProductCategoryResponse> createProductCategory(
            @RequestBody CreateProductCategoryRequest createProductCategoryRequest) throws Exception {
        return new ResponseEntity<>(
                productCategoryService.createProductCategory(createProductCategoryRequest),
                HttpStatus.CREATED);
    }
}