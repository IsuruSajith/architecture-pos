package lk.ijse.dep10.pos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @Valid
    private CustomerDTO customer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Order's datetime can't be null")
    private LocalDateTime dateTime;
    @NotEmpty(message = "Item list can't be null or empty")
    private List<@Valid ItemDTO> itemList = new ArrayList<>();
}
