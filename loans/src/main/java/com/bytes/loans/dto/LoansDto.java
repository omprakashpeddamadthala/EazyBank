package com.bytes.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(
        name = "Loans",
        description = "Schema to hold Loans information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoansDto {

    @Schema(description = "Customer ID associated with the loan" , example = "9848149507")
    @NotEmpty(message = "Mobile Number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    private String mobileNumber;

    @Schema(description = "Unique Loan Number")
    @NotEmpty(message = "Loan Number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{12})",message = "LoanNumber must be 12 digits")
    private String loanNumber;

    @Schema(description = "Type of the loan",example = "Home Loan")
    @NotEmpty(message = "LoanType can not be a null or empty")
    private String loanType;

    @Schema(description = "Total loan amount",example = "500000")
    @ Positive(message = "Total loan amount should be greater than zero")
    private int totalLoan;

    @Schema(description = "Total loan amount paid",example = "200000")
    @PositiveOrZero(message = "Total loan amount paid should be equal or greater than zero")
    private int amountPaid;

    @Schema(description = "Total outstanding amount",example = "300000")
    @PositiveOrZero(message = "Total outstanding amount should be equal or greater than zero")
    private int outstandingAmount;
}
