package com.bytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Schema(
        name = "Customer Details",
        description = "Schema to hold Customer information"
)
@Data
@Builder
public class CustomerDetailsDto {

    @Schema(
            description = "Name of the customer", example = "Om Prakash"
    )
    @NotEmpty(message = "name cannot be empty")
    @Size(min = 3, max = 50, message = "name must be between 3 and 50 characters")
    private String name;

    @Schema(
            description = "Email of the customer", example = "omprakashornold@gmail.com"
    )
    @Email(message = "please provide a valid email")
    @NotEmpty(message = "email cannot be empty")
    private String email;

    @Schema(
            description = "Mobile number of the customer", example = "9848149507"
    )
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "please provide a valid  mobile number")
    private String mobileNumber;

    @Schema(
            description = "Account details of the customer"
    )
    private AccountsDto accountsDto;

    @Schema(
            description = "Cards details of the customer"
    )
    private CardsDto cardsDto;

    @Schema(
            description = "Loans details of the customer"
    )
    private LoansDto loansDto;
}
