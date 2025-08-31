package com.bytes.loans.mapper;

import com.bytes.loans.dto.LoansDto;
import com.bytes.loans.entity.Loans;

public class LoansMapper {

    public static Loans mapToLoans(LoansDto loansDto){
        return Loans.builder()
                .mobileNumber(loansDto.getMobileNumber())
                .loanNumber(loansDto.getLoanNumber())
                .loanType(loansDto.getLoanType())
                .totalLoan(loansDto.getTotalLoan())
                .amountPaid(loansDto.getAmountPaid())
                .outstandingAmount(loansDto.getOutstandingAmount())
                .build();
    }

    public static LoansDto mapToLoanDto(Loans loans){
        return LoansDto.builder()
                .mobileNumber(loans.getMobileNumber())
                .loanNumber(loans.getLoanNumber())
                .loanType(loans.getLoanType())
                .totalLoan(loans.getTotalLoan())
                .amountPaid(loans.getAmountPaid())
                .outstandingAmount(loans.getOutstandingAmount())
                .build();
    }
}
