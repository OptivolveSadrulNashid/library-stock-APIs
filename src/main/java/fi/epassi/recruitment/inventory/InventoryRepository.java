package fi.epassi.recruitment.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryModel, UUID> {

    Optional<InventoryModel> findByInventoryId(UUID inventoryId);

    List<InventoryModel> findByBookstore_StoreId(Integer storeId);

    //@Query("SELECT i FROM InventoryModel i  WHERE i.book.isbn = :bookId")
    List<InventoryModel> findByBookIsbn(UUID bookId);

//    @Query("SELECT i FROM Inventory i JOIN i.book AS books WHERE books.title = ?1")
    @Query("SELECT i FROM InventoryModel i WHERE i.book.title = :title")
    List<InventoryModel> findByBookTitle(String title);

    @Query("SELECT i FROM InventoryModel i WHERE i.book.author = :author")
    List<InventoryModel> findByBookAuthor(String author);

}
