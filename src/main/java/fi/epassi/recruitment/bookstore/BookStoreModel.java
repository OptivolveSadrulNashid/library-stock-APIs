package fi.epassi.recruitment.bookstore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    private String contact_person;

    private String open_days;
}
