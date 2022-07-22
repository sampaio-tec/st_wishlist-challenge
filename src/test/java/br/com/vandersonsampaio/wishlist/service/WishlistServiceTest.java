package br.com.vandersonsampaio.wishlist.service;

import br.com.vandersonsampaio.wishlist.model.Wishlist;
import br.com.vandersonsampaio.wishlist.model.exceptions.ItemNotFoundException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductExistsWishlistException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductLimitException;
import br.com.vandersonsampaio.wishlist.model.repository.WishlistRepository;
import br.com.vandersonsampaio.wishlist.helper.AbstractTestSuite;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WishlistServiceTest extends AbstractTestSuite {

    @Mock
    private WishlistRepository repository;

    @InjectMocks
    private WishlistService service;

    @Test
    void insertItem() throws ProductLimitException, ProductExistsWishlistException {
        when(repository.findAllByUserId(userId)).thenReturn(new ArrayList<>());

        service.insertItem(userId, productId);

        verify(repository, times(1)).findAllByUserId(userId);
        verify(repository, times(1)).save(new Wishlist(userId, productId));
    }

    @Test
    void itemExistsInWishlist() {
        when(repository.findAllByUserId(userId)).thenReturn(List.of(new Wishlist(userId, productId)));

        assertThrows(ProductExistsWishlistException.class, () -> service.insertItem(userId, productId));

        verify(repository, times(1)).findAllByUserId(userId);
        verify(repository, times(0)).save(any());
    }

    @Test
    void wishlistIsFull() {
        when(repository.findAllByUserId(userId)).thenReturn(fullList());

        assertThrows(ProductLimitException.class, () -> service.insertItem(userId, productId));

        verify(repository, times(1)).findAllByUserId(userId);
        verify(repository, times(0)).save(any());
    }

    @Test
    void removeItem() throws ItemNotFoundException {
        List<Wishlist> list = List.of(new Wishlist(userId, productId));

        when(repository.findAllByUserIdAndProductId(userId, productId)).thenReturn(list);

        service.removeItem(userId, productId);

        verify(repository, times(1)).findAllByUserIdAndProductId(userId, productId);
        verify(repository, times(1)).delete(any());
    }

    @Test
    void removeItemNotPresent() {
        List<Wishlist> list = new ArrayList<>();

        when(repository.findAllByUserIdAndProductId(userId, productId)).thenReturn(list);

        assertThrows(ItemNotFoundException.class, () -> service.removeItem(userId, productId));

        verify(repository, times(1)).findAllByUserIdAndProductId(userId, productId);
        verify(repository, times(0)).delete(any());
    }

    @Test
    void findAll() {
        List<Wishlist> expected = List.of(new Wishlist(userId, productId));

        when(repository.findAllByUserId(userId)).thenReturn(expected);

        List<Wishlist> actualList = service.findAll(userId);

        verify(repository, times(1)).findAllByUserId(userId);
        assertEquals(expected.size(), actualList.size());
        assertEquals(expected.get(0), actualList.get(0));
    }

    @Test
    void containsItem() {
        String absentProductId = "product2";
        when(repository.existsByUserIdAndProductId(userId, productId)).thenReturn(true);
        when(repository.existsByUserIdAndProductId(userId, absentProductId)).thenReturn(false);

        assertTrue(service.containsItem(userId, productId));
        assertFalse(service.containsItem(userId, absentProductId));

        verify(repository, times(2)).existsByUserIdAndProductId(eq(userId), anyString());
    }
}