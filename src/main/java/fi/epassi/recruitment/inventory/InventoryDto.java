package fi.epassi.recruitment.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    @NotNull
    private UUID inventory_id;

    @NotNull
    private UUID isbn;

    @NotNull
    private int store_id;

    @NotNull
    @Min(value = 0, message = "Inventory can not be negative")
    private int quantity;
}
