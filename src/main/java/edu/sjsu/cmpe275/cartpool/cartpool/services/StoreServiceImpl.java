package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.ConflictException;
import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.NotFoundException;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Product;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Store;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Override
    public List<Store> getStores() {
        return storeRepository.findAll();
    }

    @Override
    public Store getStore(Long id) {
        if(storeRepository.findById(id).isPresent()) {
            return storeRepository.findById(id).get();
        }else{
            throw new NotFoundException("Cannot find store.");
        }
    }

    @Override
    public List<Product> getProducts(Long id) {
        Store store = getStore(id);
        List<Product> products = store.getProducts();
        return products;
    }

    @Override
    public Store createStore(Store store) {
        if(!storeRepository.existsByName(store.getName())){
            return storeRepository.save(store);
        }else {
            throw new ConflictException("Store name already exists");
        }
    }

    @Override
    public Store updateStore(Store store) {
        if(storeRepository.findById(store.getId()).isPresent()) {
            Store updateStore = storeRepository.findById(store.getId()).get();
            if(!storeRepository.existsByName(store.getName()) || updateStore.getName().equals(store.getName())){

                updateStore.setName(store.getName());
                updateStore.setStreet(store.getStreet());
                updateStore.setCity(store.getCity());
                updateStore.setState(store.getState());
                updateStore.setZip(store.getZip());
                return storeRepository.save(updateStore);

            }else {
                throw new ConflictException("Store name already exists");
            }


        } else {
            throw new NotFoundException("Cannot find store");
        }

    }
}
