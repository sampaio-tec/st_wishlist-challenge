package br.com.vandersonsampaio.wishlist.model;

import br.com.vandersonsampaio.wishlist.helper.AbstractTestSuite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WishlistTest extends AbstractTestSuite {

    @Test
    void testEquals() {
        Wishlist wishlistOne = new Wishlist(userId, productId);
        Wishlist wishlistTwo = new Wishlist(userId, productIdB);
        Wishlist wishlistThree = new Wishlist("A", productId);

        assertNotEquals(wishlistOne, wishlistTwo);
        assertNotEquals(wishlistOne, wishlistThree);
        assertNotEquals(wishlistOne, productId);
        assertNotEquals(wishlistOne, null);
    }
}