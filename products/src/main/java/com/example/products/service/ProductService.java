package com.example.products.service;

import com.example.products.exception.ResourceNotFoundException;
import com.example.products.exception.StockExeption;
import com.example.products.model.Category;
import com.example.products.model.Product;
import com.example.products.repository.CategoryRepository;
import com.example.products.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repo;

    private final CategoryRepository repoCategory;

    public ProductService(ProductRepository repo, CategoryRepository repoCategory) {
        this.repo = repo;
        this.repoCategory = repoCategory;
    }

    public List<Product> findAll(){
        return repo.findAll();
    }

    public Optional<Product> findById(Long id){
        return repo.findById(id);
    }

    public Product save(Product product, Long categoryid){
        Category category = repoCategory.findById(categoryid).
        orElseThrow(
            ()->
                 new ResourceNotFoundException("Category not found with id:"
                 +categoryid)
        );
        product.setCategory(category);
        return repo.save(product);
    }

    public Product update(Long id, Product newProduct,Long categoryId){



        Product productBd = repo.findById(id).orElseThrow(
            ()->
            new ResourceNotFoundException("product not found"+
            "with id"+id)
        );

        //if (optional.isEmpty()){
           // return Optional.empty();
        //}

        Category category = repoCategory.findById(categoryId).orElseThrow(
            ()->
                 new ResourceNotFoundException("Category not found with id:"+
                 categoryId
                )
        );
        //Product productBd = optional.get();

        productBd.setName(newProduct.getName()+"");
        productBd.setPrice((double)newProduct.getPrice());
        productBd.setStock(newProduct.getStock());
        productBd.setCategory(category);
        return repo.save(productBd);
    }

    public Product applyDiscount(Long id, Double percentage) {
        Product product = repo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("product not found with id:" + id)
        );

        if (product.getStock() == 0) {
            throw new StockExeption("discount does not apply when product stock is zero");
        }

        double discountedPrice = product.getPrice() * (1 - percentage / 100);
        product.setPrice(discountedPrice);
        return repo.save(product);
    }

    public boolean delete(Long id){
        if(!repo.existsById(id)){
            return false;
        }
        repo.deleteById(id);
        return true;
    }

}
