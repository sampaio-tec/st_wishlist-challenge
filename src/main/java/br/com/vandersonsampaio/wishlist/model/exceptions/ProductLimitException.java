package br.com.vandersonsampaio.wishlist.model.exceptions;

public class ProductLimitException extends Exception {

    public ProductLimitException(String userId) {
        super("Maximum Number of Products Reached for this User. User ID: " + userId);
    }
}
