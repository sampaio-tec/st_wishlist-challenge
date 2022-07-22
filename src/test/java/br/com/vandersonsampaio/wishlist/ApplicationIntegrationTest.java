package br.com.vandersonsampaio.wishlist;

import br.com.vandersonsampaio.wishlist.helper.AbstractTestSuite;
import br.com.vandersonsampaio.wishlist.model.Wishlist;
import br.com.vandersonsampaio.wishlist.model.dto.WishlistResponseDTO;
import br.com.vandersonsampaio.wishlist.model.error.Error;
import br.com.vandersonsampaio.wishlist.model.repository.WishlistRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
class ApplicationIntegrationTest extends AbstractTestSuite {

    private static final String URI = "/wishlist/client/";
    private static final String URI_COMPLEMENT = "/product/";
    private final ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WishlistRepository repository;

    @Test
    void findAllEmpty() throws Exception {
        repository.deleteAll();
        MvcResult result = mockMvc.perform(get(URI + userIdB)).andExpect(status().isNoContent()).andReturn();

        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    void findAll() throws Exception {
        repository.save(new Wishlist(userIdB, productIdD));
        MvcResult result = mockMvc.perform(get(URI + userIdB)).andExpect(status().isOk()).andReturn();

        WishlistResponseDTO actual = mapper.readValue(result.getResponse().getContentAsString(), WishlistResponseDTO.class);

        assertNotNull(actual);
        assertEquals(userIdB, actual.getUserId());
        assertFalse(actual.getProducts().isEmpty());
        assertTrue(actual.getProducts().stream().anyMatch(product -> product.getProductId().equals(productIdD)));
        assertNotNull(actual.getProducts().stream().findAny().get().getInsertTime());
    }

    @Test
    void contains() throws Exception {
        repository.save(new Wishlist(userIdB, productIdD));
        MvcResult result = mockMvc.perform(get(URI + userIdB + URI_COMPLEMENT + productIdD)).andExpect(status().isOk()).andReturn();

        Boolean actual = mapper.readValue(result.getResponse().getContentAsString(), Boolean.class);

        assertNotNull(actual);
        assertTrue(actual);
    }

    @Test
    void notContains() throws Exception {
        repository.deleteAll();
        MvcResult result = mockMvc.perform(get(URI + userIdB + URI_COMPLEMENT + productIdD)).andExpect(status().isOk()).andReturn();

        Boolean actual = mapper.readValue(result.getResponse().getContentAsString(), Boolean.class);

        assertNotNull(actual);
        assertFalse(actual);
    }

    @Test
    void remove() throws Exception {
        repository.save(new Wishlist(userIdB, productIdD));

        mockMvc.perform(delete(URI + userIdB + URI_COMPLEMENT + productIdD)).andExpect(status().isNoContent());

        assertFalse(repository.existsByUserIdAndProductId(userIdB, productIdD));
    }

    @Test
    void removeException() throws Exception {
        repository.deleteAll();
        MvcResult result = mockMvc.perform(delete(URI + userIdB + URI_COMPLEMENT + productIdD)).andExpect(status().isBadRequest()).andReturn();

        Error actual = mapper.readValue(result.getResponse().getContentAsString(), Error.class);

        assertError(actual, userIdB);
    }

    @Test
    void insert() throws Exception {
        repository.deleteAll();
        MvcResult result = mockMvc.perform(post(URI + userId + URI_COMPLEMENT + productId)).andExpect(status().isCreated()).andReturn();

        Wishlist actual = mapper.readValue(result.getResponse().getContentAsString(), Wishlist.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(userId, actual.getUserId());
        assertEquals(productId, actual.getProductId());
        assertNotNull(actual.getInsertTime());
    }

    @Test
    void insertExistsProduct() throws Exception {
        repository.save(new Wishlist(userId, productId));
        MvcResult result = mockMvc.perform(post(URI + userId + URI_COMPLEMENT + productId)).andExpect(status().isBadRequest()).andReturn();

        Error actual = mapper.readValue(result.getResponse().getContentAsString(), Error.class);

        assertError(actual, userId);
    }

    @Test
    void insertFullList() throws Exception {
        Stream.iterate(1L, x -> x + 1L).limit(20)
                .forEach(i -> repository.save(new Wishlist(userId, i.toString())));

        MvcResult result = mockMvc.perform(post(URI + userId + URI_COMPLEMENT + productId)).andExpect(status().isBadRequest()).andReturn();

        Error actual = mapper.readValue(result.getResponse().getContentAsString(), Error.class);

        assertError(actual, userId);
    }

    private void assertError(Error actual, String userId) {
        assertNotNull(actual);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().contains(userId));
        assertNotNull(actual.getTimestamp());
    }
}
