package fi.epassi.recruitment.bookstore;

import fi.epassi.recruitment.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/bookstores", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class BookStoreController {
    private final BookStoreService bookStoreService;

    @GetMapping
    ApiResponse<List<BookStoreDto>> getBookstores(
            @RequestParam(value = "name", required = false) String name) {
        return ApiResponse.ok(bookStoreService.getBookStores(name));
    }

    @PostMapping
    ApiResponse<Integer> createBookStore(@RequestBody @Validated BookStoreDto bookStoreDto) {
        var storeID = bookStoreService.createBookStore(bookStoreDto);
        return ApiResponse.ok(storeID);
    }

    @PutMapping
    ApiResponse<Integer> updateBookStore(@RequestBody @Validated BookStoreDto bookStoreDto) {
        var storeID = bookStoreService.updateBookStore(bookStoreDto);
        return ApiResponse.ok(storeID);
    }

    @GetMapping("/{storeID}")
    ApiResponse<BookStoreDto> getBookStoreByID(@PathVariable("storeID") @Validated int storeID) {
        return ApiResponse.ok(bookStoreService.getBookStoreByID(storeID));
    }

    @DeleteMapping("/{storeID}")
    ApiResponse<Void> deleteBookStoreByID(@PathVariable("storeID") @Validated int storeID) {
        bookStoreService.deleteBookStoreWithID(storeID);
        return ApiResponse.ok();
    }
}
