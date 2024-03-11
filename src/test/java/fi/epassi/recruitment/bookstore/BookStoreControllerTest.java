package fi.epassi.recruitment.bookstore;

import fi.epassi.recruitment.book.BookDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import fi.epassi.recruitment.BaseIntegrationTest;

public class BookStoreControllerTest extends BaseIntegrationTest {

    private static final String BASE_PATH_V1_BOOK = "/api/v1/bookstores";
    private static final String NAME = "name";
    private static final String BASE_PATH_V1_BOOK_BY_ID = BASE_PATH_V1_BOOK + "/{storeID}";

    private static final BookStoreModel ESPO_STORE = BookStoreModel.builder()
            .storeId(221)
            .name("Nashid Store")
            .address("Espoo")
            .contact_person("Nashid")
            .open_days("From Monday to Friday")
            .build();

    private static final BookStoreModel ONLINE_STORE = BookStoreModel.builder()
            .storeId(201)
            .name("Online Book Valley")
            .address("In online")
            .contact_person("John")
            .open_days("24 hours all day")
            .build();

    @Autowired
    private BookStoreRepository bookStoreRepository;

    //Creating part 2 test
    @Test
    @SneakyThrows
    void shouldCreateBookStoreAndReturnId() {
        // Given
        var bookStoreDto = BookStoreDto.builder().store_id(223).name("The BookShelf").address("Helsinki").build();
        var bookStoreDtoJson = mapper.writeValueAsString(bookStoreDto);

        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOK);
        var request = post(requestUrl).contentType(APPLICATION_JSON).content(bookStoreDtoJson);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(notNullValue())));
    }

    @Test
    @SneakyThrows
    void shouldRespondBadRequestWhenCreatingBookStoreWithNoName() {
        // Given
        var bookStoreDto = BookStoreDto.builder().store_id(224).address("Lapland").contact_person("Juhani").build();
        var bookStoreDtoJson = mapper.writeValueAsString(bookStoreDto);

        // When
        var response = mvc.perform(post(getEndpointUrl(BASE_PATH_V1_BOOK)).contentType(APPLICATION_JSON).content(bookStoreDtoJson));

        // Then bad request since title is required.
        response.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(BAD_REQUEST.name())))
                .andExpect(jsonPath("$.violations[0].field", is("name")))
                .andExpect(jsonPath("$.violations[0].message", is("must not be blank")));
    }

    //Searching part test
    @Test
    @SneakyThrows
    void shouldRespondWithAllBookStores() {
        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOK);
        var request = get(requestUrl).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(notNullValue())));
    }

    @Test
    @SneakyThrows
    void shouldRespondBookStoreWhenSearchingByName() {
        // Given
        bookStoreRepository.save(ESPO_STORE);

        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOK);
        var request = get(requestUrl).queryParam(NAME, "Nashid Store").contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response[0].name", is("Nashid Store")));
    }

    @Test
    @SneakyThrows
    void shouldRespondWithEmptyResponseWhenSearchingForNonExistingBookStoresByName() {
        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOK);
        var request = get(requestUrl).queryParam(NAME, "Stephen King").contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(empty())));
    }

    @Test
    @SneakyThrows
    void shouldRespondWithFoundWhenSearchingForNonExistingBookByStoreID() {
        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOK_BY_ID);

        var request = get(requestUrl, 8989).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

    //For deleting part
    @Test
    @SneakyThrows
    void shouldDeleteBookByIDSuccessfully() {
        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOK_BY_ID);

        var request = delete(requestUrl, 8989).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void shouldRespondWithBadRequestWhenDeletingWhereIDIsNotINTEGER() {
        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOK_BY_ID);
        var request = delete(requestUrl, "blaha").contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is4xxClientError());
    }

    //For updating part
    @Test
    @SneakyThrows
    void shouldUpdateExistingBookStoreSuccessfully() {
        // Given
        var saved = bookStoreRepository.save(ONLINE_STORE);

        // When
        var bookStoreDto = BookStoreDto.builder().store_id(saved.getStoreId())
                .name("Online Book Valley")
                .address("In online")
                .contact_person("Tuttoi")
                .open_days("24 hours all day")
                .build();
        var bookStoreDtoJson = mapper.writeValueAsString(bookStoreDto);

        var response = mvc.perform(put(getEndpointUrl(BASE_PATH_V1_BOOK)).contentType(APPLICATION_JSON).content(bookStoreDtoJson));

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status_code", is(OK.value())));
    }

    @Test
    @SneakyThrows
    void shouldRespondWithNotFoundWhenUpdatingNonExistingBookStore() {
        // Given
        var bookStoreDto = BookStoreDto.builder()
                .store_id(777)
                .name("LPR Book Valley")
                .address("Lappeenranta")
                .build();
        var bookStoreDtoJson = mapper.writeValueAsString(bookStoreDto);

        // When
        var response = mvc.perform(put(getEndpointUrl(BASE_PATH_V1_BOOK)).contentType(APPLICATION_JSON).content(bookStoreDtoJson));

        // Then
        response.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

}
