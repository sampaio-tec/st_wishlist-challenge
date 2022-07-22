package br.com.vandersonsampaio.wishlist.service.interfaces;

import br.com.vandersonsampaio.wishlist.model.Wishlist;
import br.com.vandersonsampaio.wishlist.model.exceptions.ItemNotFoundException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductExistsWishlistException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductLimitException;

import java.util.List;

public interface IWishlistService {

    Wishlist insertItem(String userId, String productId) throws ProductLimitException, ProductExistsWishlistException;
    void removeItem(String userId, String productId) throws ItemNotFoundException;

    List<Wishlist> findAll(String userId);
    boolean containsItem(String userId, String productId);
}
