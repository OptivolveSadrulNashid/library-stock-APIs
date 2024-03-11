package fi.epassi.recruitment.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class BookStoreNotFoundException extends ApplicationException {

    public BookStoreNotFoundException(final String storeID) {
        super(NOT_FOUND, "No book store found with storeID: {%s}".formatted(storeID));
    }
}
