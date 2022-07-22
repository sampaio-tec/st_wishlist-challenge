package br.com.vandersonsampaio.wishlist.controller;

import br.com.vandersonsampaio.wishlist.helper.AbstractTestSuite;
import br.com.vandersonsampaio.wishlist.model.Wishlist;
import br.com.vandersonsampaio.wishlist.model.dto.ProductResponseDTO;
import br.com.vandersonsampaio.wishlist.model.dto.WishlistResponseDTO;
import br.com.vandersonsampaio.wishlist.model.exceptions.ItemNotFoundException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductExistsWishlistException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductLimitException;
import br.com.vandersonsampaio.wishlist.service.interfaces.IWishlistService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistControllerTest extends AbstractTestSuite {

    @Mock
    private IWishlistService service;

    @InjectMocks
    private WishlistController controller;

    @Test
    void insertItem() throws ProductLimitException, ProductExistsWishlistException {
        LocalDateTime expectedTime = LocalDateTime.now();
        String expectedId = "Id1";

        when(service.insertItem(userId, productId)).thenReturn(new Wishlist(expectedId, userId, productId, expectedTime));

        ResponseEntity<Wishlist> actual = controller.insertItem(userId, productId);

        assertEquals(HttpStatus.CREATED, actual.getStatusCode());

        assertNotNull(actual.getBody());
        assertEquals(expectedId, actual.getBody().getId());
        assertEquals(userId, actual.getBody().getUserId());
        assertEquals(productId, actual.getBody().getProductId());
        assertEquals(expectedTime, actual.getBody().getInsertTime());
    }

    @Test
    void insertItemFailedLimit() throws ProductLimitException, ProductExistsWishlistException {
        when(service.insertItem(userId, productId)).thenThrow(new ProductLimitException(userId));

        assertThrows(ProductLimitException.class, () -> controller.insertItem(userId, productId));

        verify(service, times(1)).insertItem(userId, productId);
    }

    @Test
    void insertItemFailedExistsWishlist() throws ProductLimitException, ProductExistsWishlistException {
        when(service.insertItem(userId, productId)).thenThrow(new ProductExistsWishlistException(userId, productId));

        assertThrows(ProductExistsWishlistException.class, () -> controller.insertItem(userId, productId));

        verify(service, times(1)).insertItem(userId, productId);
    }

    @Test
    void removeItem() throws ItemNotFoundException {
        ResponseEntity<?> actual = controller.removeItem(userId, productId);

        verify(service, times(1)).removeItem(userId, productId);

        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
    }

    @Test
    void removeItemNotFound() throws ItemNotFoundException {
        doThrow(new ItemNotFoundException(userId, productId)).when(service).removeItem(userId, productId);

        assertThrows(ItemNotFoundException.class, () -> controller.removeItem(userId, productId));

        verify(service, times(1)).removeItem(userId, productId);
    }

    @Test
    void containsItem() {
        when(service.containsItem(userId, productId)).thenReturn(true);

        ResponseEntity<?> actual = controller.containsItem(userId, productId);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(Boolean.TRUE, actual.getBody());
    }

    @Test
    void listAllItems() {
        when(service.findAll(userId)).thenReturn(buildWishlist());

        ResponseEntity<WishlistResponseDTO> actual = controller.listAllItems(userId);

        verify(service, times(1)).findAll(userId);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNotNull(actual.getBody());
        assertEquals(userId, actual.getBody().getUserId());
        assertEquals(5, actual.getBody().getProducts().size());
        assertProducts(actual.getBody().getProducts(), productId, productIdB, productIdC, productIdD, productIdE);
    }

    @Test
    void listAllItemsEmpty() {
        when(service.findAll(userId)).thenReturn(new ArrayList<>());

        ResponseEntity<WishlistResponseDTO> actual = controller.listAllItems(userId);

        verify(service, times(1)).findAll(userId);

        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
    }

    private void assertProducts(List<ProductResponseDTO> productList, String... products) {
        for(String product : products) {
            assertTrue(productList.stream().anyMatch(productResponse -> productResponse.getProductId().equals(product)));
        }
    }
}