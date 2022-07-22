package br.com.vandersonsampaio.wishlist.service;

import br.com.vandersonsampaio.wishlist.model.Wishlist;
import br.com.vandersonsampaio.wishlist.model.exceptions.ItemNotFoundException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductExistsWishlistException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductLimitException;
import br.com.vandersonsampaio.wishlist.model.repository.WishlistRepository;
import br.com.vandersonsampaio.wishlist.service.interfaces.IWishlistService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService implements IWishlistService {

    private int limitWishlist = 20;
    private final WishlistRepository repository;

    public WishlistService(WishlistRepository repository) {
        this.repository = repository;
    }

    @Override
    public Wishlist insertItem(String userId, String productId) throws ProductLimitException, ProductExistsWishlistException {
        List<Wishlist> allItems = findAll(userId);

        if(allItems.size() == limitWishlist) {
            throw new ProductLimitException(userId);
        } else if (containsItem(allItems, productId)) {
            throw new ProductExistsWishlistException(userId, productId);
        } else {
            return repository.save(new Wishlist(userId, productId));
        }
    }

    @Override
    public void removeItem(String userId, String productId) throws ItemNotFoundException {
        List<Wishlist> allByUserId = repository.findAllByUserIdAndProductId(userId, productId);
        Optional<Wishlist> item = allByUserId.stream().findAny();

        if(item.isPresent()) {
            repository.delete(item.get());
        } else {
            throw new ItemNotFoundException(userId, productId);
        }
    }

    @Override
    public List<Wishlist> findAll(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public boolean containsItem(String userId, String productId) {
        return repository.existsByUserIdAndProductId(userId, productId);
    }

    private boolean containsItem(List<Wishlist> items, String productId) {
        return items.stream().anyMatch(item -> item.getProductId().equals(productId));
    }
}
