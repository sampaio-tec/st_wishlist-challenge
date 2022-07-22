package br.com.vandersonsampaio.wishlist.model.exceptions;

public class ItemNotFoundException extends Exception {

    public ItemNotFoundException(String userId, String productId) {
        super("Item not found in wishlist. User ID: " + userId + " Product ID: " + productId);
    }
}
