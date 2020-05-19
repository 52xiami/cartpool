package edu.sjsu.cmpe275.cartpool.cartpool.controllers;

import edu.sjsu.cmpe275.cartpool.cartpool.models.CartPool;
import edu.sjsu.cmpe275.cartpool.cartpool.services.CartPoolService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cartpool")
public class CartPoolController {
    private final CartPoolService cartPoolService;

    /**
     * Retrieve all cartpools
     * /api/cartpool
     */
    @GetMapping("")
    public List<CartPool> getCartPools() {
        return cartPoolService.getCartPools();
    }


    /**
     * Retrieve cartpool by id
     * /api/cartpool/{id}
     */
    @GetMapping("{id}")
    public CartPool getCartPool(@PathVariable("id") @NotNull Long id) {
        return cartPoolService.getCartPool(id);
    }

    /**
     * Add a cartpool
     * /api/cartpool/
     */
    @PostMapping("")
    public CartPool createCartPool(@RequestParam(value = "pool_id") @NotNull @Length(max = 50) String poolId,
                                   @RequestParam(value = "name") @NotNull @Length(max = 100) String name,
                                   @RequestParam(value = "neighborhood") @NotNull @Length(max = 100) String neighborhood,
                                   @RequestParam(value = "description") @Length(max = 500) String description,
                                   @RequestParam(value = "zip") @NotNull @Length(max = 5) String zip,
                                   @RequestParam(value = "leader") Long leader) {

        CartPool cartPool = new CartPool();
        cartPool.setPoolId(poolId);
        cartPool.setName(name);
        cartPool.setNeighborhood(neighborhood);
        cartPool.setDescription(description);
        cartPool.setZip(zip);
        cartPool.setLeader(leader);

        return cartPoolService.createCartPool(cartPool);
    }

    /**
     * Update a cartpool
     * /api/cartpool/
     */
    @PutMapping("{id}")
    public CartPool updateCartPool(@PathVariable("id") Long id) {
        return null;
    }

    /**
     * Delete a cartpool
     * /api/cartpool/
     */
    @DeleteMapping("{id}")
    public CartPool deleteCartPool(@PathVariable("id") Long id) {
        return cartPoolService.deleteCartPool(id);
    }





}
