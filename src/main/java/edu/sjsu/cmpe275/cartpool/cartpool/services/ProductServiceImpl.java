package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.NotFoundException;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Inventory;
import edu.sjsu.cmpe275.cartpool.cartpool.models.InventoryId;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Product;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.InventoryRepository;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        if(productRepository.findById(id).isPresent()) {
            return productRepository.findById(id).get();
        }else{
            throw new NotFoundException("Cannot find product");
        }
    }

    @Override
    public List<Product> getProducts(Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage,pageSize);

        return productRepository.findAllBy(pageable);
    }

    @Override
    public boolean addProduct(Product product) {
        productRepository.save(product);
        return true;
    }

    @Override
    public Inventory addProductToStore(Long productId, Long storeID) {
        InventoryId inventoryId = new InventoryId();
        inventoryId.setProductId(productId);
        inventoryId.setStoreId(storeID);
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);
        inventory.setQuantity(0);
        inventoryRepository.save(inventory);
        return inventory;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if(productRepository.findById(product.getProductId()).isPresent()) {
            Product updateProduct = productRepository.findById(product.getProductId()).get();

            updateProduct.setName(product.getName());
            updateProduct.setDescription(product.getDescription());
            updateProduct.setImageUrl(product.getImageUrl());
            updateProduct.setBrand(product.getBrand());
            updateProduct.setCategory(product.getCategory());
            updateProduct.setPrice(product.getPrice());
            updateProduct.setUnit(product.getUnit());

            return productRepository.save(updateProduct);

        } else {
            throw new NotFoundException("Cannot find product");
        }
    }

}
