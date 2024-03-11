package fi.epassi.recruitment.book;

import fi.epassi.recruitment.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/books", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService bookService;

    @GetMapping
    ApiResponse<Page<BookDto>> getBooks(
        @RequestParam(value = "author", required = false) String author,
        @RequestParam(value = "title", required = false) String title,
        Pageable pageable) {
        return ApiResponse.ok(bookService.getBooks(author, title,pageable));
    }

    @PostMapping
    ApiResponse<UUID> createBook(@RequestBody @Validated BookDto bookDto) {
        var isbn = bookService.createBook(bookDto);
        return ApiResponse.ok(isbn);
    }

    @PutMapping
    ApiResponse<UUID> updateBook(@RequestBody @Validated BookDto bookDto) {
        var ret = bookService.updateBook(bookDto);
        return ApiResponse.ok(ret);
    }

    @GetMapping("/{isbn}")
    ApiResponse<BookDto> getBookByIsbn(@PathVariable("isbn") @Validated UUID isbn) {
        return ApiResponse.ok(bookService.getBookByIsbn(isbn));
    }

    @DeleteMapping("/{isbn}")
    ApiResponse<Void> deleteBookByIsbn(@PathVariable("isbn") @Validated UUID isbn) {
        bookService.deleteBookWithIsbn(isbn);
        return ApiResponse.ok();
    }

//    @PutMapping("/quantity")
//    ApiResponse<UUID> updateBookQuantity(@RequestBody @Validated BookQuantityDto bookQuantityDto) {
//        var ret = bookService.updateBookQuantity(bookQuantityDto);
//        return ApiResponse.ok(ret);
//    }
//
//    @GetMapping("/isbn")
//    ApiResponse<Integer> getBookQuantityByIsbn(@RequestParam UUID isbn){
//        return ApiResponse.ok(bookService.getBookQuantityByIsbn(isbn));
//    }
//
//    @GetMapping("/author")
//    ApiResponse<Integer> getBookQuantityByAuthor(@RequestParam String author) {
//        return ApiResponse.ok(bookService.getBookQuantityByAuthor(author));
//    }
//
//    @GetMapping("/title")
//    ApiResponse<Integer> getBookByQuantityTitle(@RequestParam String title) {
//        return ApiResponse.ok(bookService.getBookQuantityByTitle(title));
//    }

}