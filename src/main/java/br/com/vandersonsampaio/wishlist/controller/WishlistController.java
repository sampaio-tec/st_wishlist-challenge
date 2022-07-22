package br.com.vandersonsampaio.wishlist.controller;

import br.com.vandersonsampaio.wishlist.model.Wishlist;
import br.com.vandersonsampaio.wishlist.model.dto.ProductResponseDTO;
import br.com.vandersonsampaio.wishlist.model.dto.WishlistResponseDTO;
import br.com.vandersonsampaio.wishlist.model.exceptions.ItemNotFoundException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductExistsWishlistException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductLimitException;
import br.com.vandersonsampaio.wishlist.service.interfaces.IWishlistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/wishlist")
@AllArgsConstructor
public class WishlistController {

    private final IWishlistService service;

    @PostMapping(value = "client/{userId}/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wishlist> insertItem(@PathVariable(value="userId") String userId,
                                        @PathVariable(value="productId") String productId) throws ProductLimitException, ProductExistsWishlistException {
        Wishlist wishlist = service.insertItem(userId, productId);
        return new ResponseEntity<>(wishlist, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "client/{userId}/product/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable(value="userId") String userId,
                                        @PathVariable(value="productId") String productId) throws ItemNotFoundException {
        service.removeItem(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "client/{userId}/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> containsItem(@PathVariable(value="userId") String userId,
                                          @PathVariable(value="productId") String productId) {
        return ResponseEntity.ok(service.containsItem(userId, productId));
    }

    @GetMapping(value = "client/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WishlistResponseDTO> listAllItems(@PathVariable(value="userId") String userId) {
        List<Wishlist> all = service.findAll(userId);

        return all.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(parserToDTO(all, userId));
    }

    private WishlistResponseDTO parserToDTO(List<Wishlist> list, String userId) {
        return new WishlistResponseDTO(userId, list.stream()
                .map(item -> new ProductResponseDTO(item.getProductId(), item.getInsertTime())).toList());
    }
}
