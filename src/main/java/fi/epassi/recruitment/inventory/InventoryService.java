package fi.epassi.recruitment.inventory;

import fi.epassi.recruitment.book.BookRepository;
import fi.epassi.recruitment.bookstore.BookStoreRepository;
import fi.epassi.recruitment.exception.InventoryNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final BookStoreRepository bookStoreRepository;
    private final BookRepository bookRepository;
    private final InventoryRepository inventoryRepository;

    public UUID createInventory(InventoryDto inventoryDto) {
        InventoryModel inventoryModel = toInventoryModel(inventoryDto);
        var savedInventory = inventoryRepository.save(inventoryModel);
        return savedInventory.getInventoryId();
    }

    public UUID updateInventory(InventoryDto inventoryDto) {
        if (inventoryRepository.findByInventoryId(inventoryDto.getInventory_id()).isPresent()) {
            var i = toInventoryModel(inventoryDto);
            var savedBook = inventoryRepository.save(i);
            return savedBook.getInventoryId();
        }

        throw new InventoryNotFoundException(inventoryDto.getInventory_id().toString());
    }

    public void deleteInventoryWithID(@NonNull UUID inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    public List<InventoryDto> getInventories() {

        return inventoryRepository.findAll()
                .stream()
                .map(InventoryService::toInventoryDto)
                .toList();
    }

    public InventoryDto getInventoryById(@NonNull UUID inventoryId) throws InventoryNotFoundException {
        return inventoryRepository.findByInventoryId(inventoryId)
                .map(InventoryService::toInventoryDto)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId.toString()));
    }

    public int getBookQuantityByIsbn(@NonNull UUID isbn){
        //int size = inventoryRepository.findByBookIsbn(isbn).size();
        int sum = inventoryRepository
                .findByBookIsbn(isbn)
                .stream()
                .mapToInt(InventoryModel::getQuantity)
                .sum();
        return sum;
    }

    public int getBookQuantityByAuthor(@NonNull String author){
        return inventoryRepository.findByBookAuthor(author)
                .stream()
                .mapToInt(InventoryModel::getQuantity)
                .sum();
    }

    public int getBookQuantityByTitle(@NonNull String title){
        return inventoryRepository.findByBookTitle(title)
                .stream()
                .mapToInt(InventoryModel::getQuantity)
                .sum();
    }

    public int getBookQuantityByStoreId(@NonNull int storeId){
        return inventoryRepository
                .findByBookstore_StoreId(storeId)
                .stream()
                .mapToInt(InventoryModel::getQuantity)
                .sum();
    }

//    public int getQuantity(String author, String title, UUID Isbn, int storeId){
//        if (StringUtils.isNotBlank(author)){
//            return getBookQuantityByAuthor(author);
//        }else if (StringUtils.isNotBlank(title)){
//            return getBookQuantityByTitle(author);
//        }
//        else {
//            throw new ApplicationException(HttpStatus.NOT_ACCEPTABLE, "Can not accept more than one parameter");
//        }
//    }

    private InventoryModel toInventoryModel(InventoryDto inventoryDto) {
        var nn = bookStoreRepository
                .findByStoreId(inventoryDto.getStore_id())
                .get();
        var book = bookRepository
                .findByIsbn(inventoryDto.getIsbn())
                .get();
        return InventoryModel.builder()
                .inventoryId(inventoryDto.getInventory_id())
                .bookstore(nn)
                .book(book)
                .quantity(inventoryDto.getQuantity())
                .build();
    }

    private static InventoryDto toInventoryDto(InventoryModel inventoryModel) {

        return InventoryDto.builder()
                .inventory_id(inventoryModel.getInventoryId())
                .isbn(inventoryModel.getBook().getIsbn())
                .store_id(inventoryModel.getBookstore().getStoreId())
                .quantity(inventoryModel.getQuantity())
                .build();
    }
}
