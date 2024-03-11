package fi.epassi.recruitment.bookstore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookStoreDto {

    @NotNull
    private int store_id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    private String contact_person;

    private String open_days;
}
