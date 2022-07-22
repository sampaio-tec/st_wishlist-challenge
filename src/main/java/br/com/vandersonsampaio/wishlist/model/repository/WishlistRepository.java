package br.com.vandersonsampaio.wishlist.model.repository;

import br.com.vandersonsampaio.wishlist.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {

    List<Wishlist> findAllByUserId(String userId);
    boolean existsByUserIdAndProductId(String userId, String productId);
    List<Wishlist> findAllByUserIdAndProductId(String userId, String productId);
}
