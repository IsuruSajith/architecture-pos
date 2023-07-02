package lk.ijse.dep10.pos.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Customer DTO JSON Object")
public class CustomerDTO {
    @ApiModelProperty(notes = "Customer ID should not be specified when saving")
    private Integer id;
//    @NotNull
//    @NotEmpty = @NotNull + Can't be empty (but, white spaces are okay)
//    @NotBlank = @NotEmpty + Can't contain only white spaces
    @NotBlank(message = "Name can't be empty or null")
    @Pattern(regexp = "[A-Za-z ]+", message = "Invalid name")
    @ApiModelProperty(notes = "Customer name")
    private String name;
    @NotBlank(message = "Address can't be empty or null")
    @Length(min = 3, message = "Invalid address")
    private String address;
    @NotBlank(message = "Contact number can't be empty or null")
    @Pattern(regexp = "^\\d{3}-\\d{7}$", message = "Invalid contact number")
    private String contact;
}
