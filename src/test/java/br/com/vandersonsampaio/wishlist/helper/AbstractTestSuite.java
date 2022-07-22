package br.com.vandersonsampaio.wishlist.helper;

import br.com.vandersonsampaio.wishlist.model.Wishlist;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractTestSuite {
    protected String userId = "user1";
    protected String userIdB = "userB";
    protected String productId = "product1";
    protected String productIdB = "product1B";
    protected String productIdC = "product1C";
    protected String productIdD = "product1D";
    protected String productIdE = "product1E";

    protected List<Wishlist> fullList() {
        return Stream.generate(Wishlist::new).limit(20).collect(Collectors.toList());
    }

    protected List<Wishlist> buildWishlist() {
        return List.of(new Wishlist(userId, productId),
                new Wishlist(userId, productIdB),
                new Wishlist(userId, productIdC),
                new Wishlist(userId, productIdD),
                new Wishlist(userId, productIdE));
    }
}
