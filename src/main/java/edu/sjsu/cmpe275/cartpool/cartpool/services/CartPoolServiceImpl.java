package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.ConflictException;
import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.NotFoundException;
import edu.sjsu.cmpe275.cartpool.cartpool.models.CartPool;
import edu.sjsu.cmpe275.cartpool.cartpool.models.User;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.CartPoolRepository;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartPoolServiceImpl implements CartPoolService {
    private final CartPoolRepository cartPoolRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public List<CartPool> getCartPools() {
        return cartPoolRepository.findAll();
    }

    @Override
    public CartPool getCartPool(Long id) {
        if(cartPoolRepository.findById(id).isPresent()){
            return cartPoolRepository.findById(id).get();
        }else {
            throw new NotFoundException("Cannot find cart pool");
        }
    }

    @Transactional
    @Override
    public CartPool createCartPool(CartPool cartPool) {
        if(cartPoolRepository.existsByPoolId(cartPool.getPoolId())){
            throw new ConflictException("Pool ID already exist");
        }else{
            CartPool newCartPool = cartPoolRepository.save(cartPool);

            userService.joinCartPool(newCartPool.getLeader(), newCartPool.getId());

            return newCartPool;
        }
    }

    @Transactional
    @Override
    public CartPool deleteCartPool(Long id) {
        if(cartPoolRepository.findById(id).isPresent()){
            CartPool cartPool = cartPoolRepository.findById(id).get();
            List<User> users = userRepository.findByPoolId(id);
            if(users.size() == 1 && users.get(0).getPoolId().equals(id)){
                cartPoolRepository.deleteById(id);
                users.get(0).setPoolId(null);
                users.get(0).setPoolStatus("INIT");
                userRepository.save(users.get(0));
                return cartPool;
            }else{
                throw new ConflictException("Cannot delete pool, since there are members in this pool");
            }
        }else {
            throw new NotFoundException("Cannot find cart pool");
        }
    }
}
