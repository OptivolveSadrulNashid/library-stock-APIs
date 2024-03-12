package fi.epassi.recruitment.inventory;

import fi.epassi.recruitment.api.ApiResponse;
import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.bookstore.BookStoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/inventory", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    ApiResponse<List<InventoryDto>> getInventories() {
        return ApiResponse.ok(inventoryService.getInventories());
    }

    @GetMapping("/{id}")
    ApiResponse<InventoryDto> getInventoryById(@PathVariable("id") @Validated UUID id) {
        return ApiResponse.ok(inventoryService.getInventoryById(id));
    }

    @PostMapping
    ApiResponse<UUID> createInventory(@RequestBody @Validated InventoryDto inventoryDto) {
        var id = inventoryService.createInventory(inventoryDto);
        return ApiResponse.ok(id);
    }

    @PutMapping
    ApiResponse<UUID> updateInventory(@RequestBody @Validated InventoryDto inventoryDto) {
        var id = inventoryService.updateInventory(inventoryDto);
        return ApiResponse.ok(id);
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteInventoryById(@PathVariable("id") @Validated UUID id) {
        inventoryService.deleteInventoryWithID(id);
        return ApiResponse.ok();
    }

    @GetMapping("/isbn")
    ApiResponse<Integer> getBookQuantityByIsbn(@RequestParam @Validated UUID isbn){
        return ApiResponse.ok(inventoryService.getBookQuantityByIsbn(isbn));
    }

    @GetMapping("/author")
    ApiResponse<Integer> getBookQuantityByAuthor(@RequestParam String author) {
        return ApiResponse.ok(inventoryService.getBookQuantityByAuthor(author));
    }

    @GetMapping("/title")
    ApiResponse<Integer> getBookByQuantityByTitle(@RequestParam String title) {
        return ApiResponse.ok(inventoryService.getBookQuantityByTitle(title));
    }

    @GetMapping("/storeId")
    ApiResponse<Integer> getBookByQuantityByStoreId(@RequestParam int storeId) {
        return ApiResponse.ok(inventoryService.getBookQuantityByStoreId(storeId));
    }

}
