package com.example.products.controller;

import com.example.products.dto.ProductRequest;
import com.example.products.dto.ProductResponse;
import com.example.products.dto.RequestDiscountProduct;
import com.example.products.exception.ResourceNotFoundException;
import com.example.products.model.Product;
import com.example.products.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping ()
    public ResponseEntity<List<ProductResponse>> findAll(){
        List<ProductResponse> list = new ArrayList<>();
        for(Product elem : service.findAll()){
            list.add(toResponse(elem));
        }
        

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        Product product = service.findById(id).orElseThrow(
            ()-> new ResourceNotFoundException("product not found with id:"+id)
        );
        
        return ResponseEntity.ok(toResponse(product));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest productRequest) {
        Product productSave = service.save(toEntity(productRequest),productRequest.categoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(productSave));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest productUpdate) {
        
        Product product = service.update(id, 
            toEntity(productUpdate),
            productUpdate.categoryId()
        );
        

        return ResponseEntity.ok(toResponse(product));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> applyDiscount(
        @PathVariable Long id,
        @Valid @RequestBody RequestDiscountProduct discountRequest
    ) {
        Product updatedProduct = service.applyDiscount(id, discountRequest.percentage());
        return ResponseEntity.ok(toResponse(updatedProduct));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if(!deleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private ProductResponse toResponse(Product product){
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getStock(),
            product.getCategory().getName(),
            product.getCategory().getId()
        );
    }

     private Product toEntity(ProductRequest product){
        Product productEntity = new Product();
        productEntity.setName(product.name());
        productEntity.setPrice(product.price());
        productEntity.setStock(product.stock());
        return productEntity;
    }




}
