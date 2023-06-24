package lk.ijse.dep10.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private String code;
    private String description;
    private int qty;
    private BigDecimal unitPrice;
}
