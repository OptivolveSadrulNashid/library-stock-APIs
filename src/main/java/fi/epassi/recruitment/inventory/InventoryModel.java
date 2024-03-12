package fi.epassi.recruitment.inventory;

import fi.epassi.recruitment.book.BookModel;
import fi.epassi.recruitment.bookstore.BookStoreModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.UUID;

import static java.sql.Types.VARCHAR;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "inventory")
public class InventoryModel {

    @Id
    @Column(name = "inventory_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(value = VARCHAR)
    private UUID inventoryId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private BookStoreModel bookstore;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookModel book;

    @Column(nullable = false)
    @Min(value = 0, message = "Quantity of books can not be negative")
    private int quantity;
}
