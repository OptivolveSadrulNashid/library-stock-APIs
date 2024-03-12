package fi.epassi.recruitment.bookstore;

import fi.epassi.recruitment.inventory.InventoryModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "bookstore")
public class BookStoreModel {

    @Id
    @Column(name = "store_id")
    private int storeId;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    private String contactPerson;

    private String openDays;

    @OneToMany(mappedBy = "bookstore")
    private List<InventoryModel> inventory;
}
