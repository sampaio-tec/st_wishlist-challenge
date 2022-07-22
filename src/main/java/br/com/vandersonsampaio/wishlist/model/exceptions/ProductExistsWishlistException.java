package br.com.vandersonsampaio.wishlist.model.exceptions;

public class ProductExistsWishlistException extends Exception {

    public ProductExistsWishlistException(String userId, String productId) {
        super("The product already belongs to the wishlist. User ID: " + userId + " Product ID: " + productId);
    }
}
