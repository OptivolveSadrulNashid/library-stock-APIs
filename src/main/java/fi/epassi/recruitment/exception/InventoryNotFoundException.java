package fi.epassi.recruitment.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class InventoryNotFoundException extends ApplicationException {

    public InventoryNotFoundException(final String inventoryId) {
        super(NOT_FOUND, "No inventory found with ID {%s}".formatted(inventoryId));
    }
}
