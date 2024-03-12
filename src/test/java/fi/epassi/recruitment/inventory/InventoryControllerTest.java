package fi.epassi.recruitment.inventory;

import fi.epassi.recruitment.BaseIntegrationTest;
import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.book.BookModel;
import fi.epassi.recruitment.book.BookRepository;
import fi.epassi.recruitment.bookstore.BookStoreModel;
import fi.epassi.recruitment.bookstore.BookStoreRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InventoryControllerTest extends BaseIntegrationTest {
    private static final String API_V_1_INVENTORY = "/api/v1/inventory";
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String BASE_PATH_V1_BOOK_BY_ISBN = API_V_1_INVENTORY + "/{isbn}";

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookStoreRepository bookStoreRepository;

    private static final BookModel BOOK_HOBBIT = BookModel.builder()
            .isbn(UUID.fromString("66737096-39ef-4a7c-aa4a-9fd018c14178"))
            .title("The Hobbit")
            .author("J.R.R Tolkien")
            .price(TEN)
            .build();

    private static final BookModel BOOK_FELLOWSHIP = BookModel.builder()
            .isbn(UUID.fromString("556aa37d-ef9c-45d3-ba4a-a792c123208a"))
            .title("The Fellowship of the Rings")
            .author("J.R.R Tolkien")
            .price(TEN)
            .build();

    private static final BookStoreModel ESPO_STORE = BookStoreModel.builder()
            .storeId(221)
            .name("Nashid Store")
            .address("Espoo")
            .contactPerson("Nashid")
            .openDays("From Monday to Friday")
            .build();

    private static final BookStoreModel ONLINE_STORE = BookStoreModel.builder()
            .storeId(201)
            .name("Online Book Valley")
            .address("In online")
            .contactPerson("John")
            .openDays("24 hours all day")
            .build();

    private static final InventoryModel MODEL_1 = InventoryModel.builder()
            .inventoryId(UUID.fromString("556aa37d-ef9c-45d3-ba4a-a792c123209b"))
            .book(BOOK_HOBBIT)
            .bookstore(ONLINE_STORE)
            .quantity(20)
            .build();

    private static final InventoryModel MODEL_2 = InventoryModel.builder()
            .inventoryId(UUID.fromString("556aa37d-ef9c-45d3-ba4a-a792c123209f"))
            .book(BOOK_FELLOWSHIP)
            .bookstore(ONLINE_STORE)
            .quantity(35)
            .build();


    @Test
    @SneakyThrows
    void shouldCreateInventoryAndReturnId() {
        // Given
        bookRepository.save(BOOK_HOBBIT);
        bookStoreRepository.save(ESPO_STORE);

        var inventoryDto = InventoryDto.builder()
                .inventory_id(UUID.randomUUID())
                .isbn(UUID.fromString("66737096-39ef-4a7c-aa4a-9fd018c14178"))
                .store_id(221).quantity(5).build();
        var inventoryDtoJson = mapper.writeValueAsString(inventoryDto);

        // When
        var requestUrl = getEndpointUrl(API_V_1_INVENTORY);
        var request = post(requestUrl).contentType(APPLICATION_JSON).content(inventoryDtoJson);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(notNullValue())));
    }

    @Test
    @SneakyThrows
    void shouldRespondBadRequestWhenCreatingNegativeInventory() {
        // Given
        bookRepository.save(BOOK_HOBBIT);
        bookStoreRepository.save(ESPO_STORE);

        var inventoryDto = InventoryDto.builder()
                .inventory_id(UUID.randomUUID())
                .isbn(UUID.fromString("66737096-39ef-4a7c-aa4a-9fd018c14178"))
                .store_id(221)
                .quantity(-7)
                .build();
        var inventoryDtoJson = mapper.writeValueAsString(inventoryDto);

        // When
        var response = mvc.perform(post(getEndpointUrl(API_V_1_INVENTORY)).contentType(APPLICATION_JSON).content(inventoryDtoJson));

        // Then bad request since title is required.
        response.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(BAD_REQUEST.name())))
                .andExpect(jsonPath("$.violations[0].field", is("quantity")))
                .andExpect(jsonPath("$.violations[0].message", is("Inventory can not be negative")));
    }

    //get quantity test

    @Test
    @SneakyThrows
    void shouldGetBookQuantityByIsbn() {
        // Given
        bookRepository.save(BOOK_HOBBIT);
        bookRepository.save(BOOK_FELLOWSHIP);
        bookStoreRepository.save(ONLINE_STORE);
        inventoryRepository.save(MODEL_1);
        inventoryRepository.save(MODEL_2);

        // When
        var requestUrl = getEndpointUrl(API_V_1_INVENTORY + "/isbn");
        var request = get(requestUrl).queryParam("isbn", "66737096-39ef-4a7c-aa4a-9fd018c14178").contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(20)));
    }



    @Test
    @SneakyThrows
    void shouldGetBookQuantityByAuthor() {
        // Given
        bookRepository.save(BOOK_HOBBIT);
        bookRepository.save(BOOK_FELLOWSHIP);
        bookStoreRepository.save(ONLINE_STORE);
        inventoryRepository.save(MODEL_1);
        inventoryRepository.save(MODEL_2);
        var author = "J.R.R Tolkien";

        // When
        var requestUrl = getEndpointUrl(API_V_1_INVENTORY + "/author");
        var request = get(requestUrl).queryParam("author", author).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(notNullValue())))
        .andExpect(jsonPath("$.response", is(55)));
    }

    @Test
    @SneakyThrows
    void shouldGetBookQuantityByTitle() {
        // Given
        bookRepository.save(BOOK_HOBBIT);
        bookRepository.save(BOOK_FELLOWSHIP);
        bookStoreRepository.save(ONLINE_STORE);
        inventoryRepository.save(MODEL_1);
        inventoryRepository.save(MODEL_2);
        var title = "The Hobbit";

        // When
        var requestUrl = getEndpointUrl(API_V_1_INVENTORY + "/title");
        var request = get(requestUrl).queryParam("title", title).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(notNullValue())))
        .andExpect(jsonPath("$.response", is(20)));
    }

    @Test
    @SneakyThrows
    void shouldGetBookQuantityByStoreId() {
        // Given
        bookRepository.save(BOOK_HOBBIT);
        bookRepository.save(BOOK_FELLOWSHIP);
        bookStoreRepository.save(ONLINE_STORE);
        inventoryRepository.save(MODEL_1);
        inventoryRepository.save(MODEL_2);

        // When
        var requestUrl = getEndpointUrl(API_V_1_INVENTORY + "/storeId");
        var request = get(requestUrl).queryParam("storeId", "201").contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.response", is(55)));
    }


    @Test
    @SneakyThrows
    void shouldUpdateExistingInventorySuccessfully() {
        // Given
        bookRepository.save(BOOK_HOBBIT);
        bookStoreRepository.save(ONLINE_STORE);
        var saved = inventoryRepository.save(MODEL_1);

        // When
        var inventoryDto = InventoryDto.builder()
                .inventory_id(saved.getInventoryId())
                .isbn(saved.getBook().getIsbn())
                .store_id(saved.getBookstore().getStoreId())
                .quantity(766)
                .build();
        var inventoryDtoJson = mapper.writeValueAsString(inventoryDto);

        var response = mvc.perform(put(getEndpointUrl(API_V_1_INVENTORY))
                .contentType(APPLICATION_JSON)
                .content(inventoryDtoJson));

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status_code", is(OK.value())));
    }

}
