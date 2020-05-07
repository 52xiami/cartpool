package edu.sjsu.cmpe275.cartpool.cartpool.controllers;

import edu.sjsu.cmpe275.cartpool.cartpool.models.Store;
import edu.sjsu.cmpe275.cartpool.cartpool.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/store")
public class StoreController {
    private final StoreService storeService;

    /**
     * Retrieve all stores
     * /api/store
     */
    @GetMapping("")
    public List<Store> getStores() {
        return storeService.getStores();
    }

    /**
     * Retrieve specific store by id
     * /api/store/{id}
     */
    @GetMapping("{id}")
    public Store getStore(@PathVariable("id") @NotNull Long id) {
        return storeService.getStore(id);
    }

    /**
     * Add a store
     * /api/store/
     */
    @PostMapping("")
    public Store createStore(@RequestBody Store store) {
        return storeService.createStore(store);
    }

    /**
     * Update a store
     * /api/store/
     */
    @PutMapping("")
    public Store updateStore(@RequestBody Store store) {
        return storeService.updateStore(store);
    }

    /**
     * Delete a store
     * /api/store/
     */
    @DeleteMapping("{id}")
    public Store deleteStore(@PathVariable("id") Long id) {
        return null;
    }

}
