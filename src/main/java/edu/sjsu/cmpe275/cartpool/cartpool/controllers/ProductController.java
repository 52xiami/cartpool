package edu.sjsu.cmpe275.cartpool.cartpool.controllers;

import edu.sjsu.cmpe275.cartpool.cartpool.models.Inventory;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Product;
import edu.sjsu.cmpe275.cartpool.cartpool.services.ProductService;
import edu.sjsu.cmpe275.cartpool.cartpool.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final StoreService storeService;

    /**
     * Retrieve all products
     * /api/product
     */
    @GetMapping("")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    /**
     * Retrieve all products by paging
     * /api/product/page
     */
    @GetMapping("/page")
    public List<Product> getProductsByPage(@RequestParam(value = "currentPage") @NotNull Integer currentPage, @RequestParam(value = "pageSize") Integer pageSize) {

        return productService.getProducts(currentPage, pageSize);
    }

    /**
     * Add product to store
     * /api/product/addToStore
     */
    @PostMapping("/addToStore")
    public Inventory addProductToStore(@RequestParam(value = "productId") @NotNull Long productId, @RequestParam(value = "storeId") Long storeId) {

        return productService.addProductToStore(productId,storeId);
    }

    /**
     * Retrieve all products by store
     * /api/product/store/{id}
     */
    @GetMapping("/store/{id}")
    public List<Product> getProductsByStore(@PathVariable("id") @NotNull Long id) {
        return storeService.getProducts(id);
    }

    /**
     * Retrieve specific product by id
     * /api/product/{id}
     */
    @GetMapping("{id}")
    public Product getProduct(@PathVariable("id") @NotNull Long id) {
        return productService.getProduct(id);
    }

    /**
     * Add a product
     * /api/product/
     */
    @PostMapping("")
    public Product createProduct(@RequestBody Product product ) {
        return productService.createProduct(product);
    }

    /**
     * Update a product
     * /api/product/
     */
    @PutMapping("")
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    /**
     * Delete a product
     * /api/product/
     */
    @DeleteMapping("{id}")
    public Product deleteProduct(@PathVariable("id") Long id) {
        return null;
    }


}
