package com.bytes.loans.service;

import com.bytes.loans.dto.LoansDto;

public interface LoansService {

    void createLoan(String mobileNumber);

    LoansDto fetchLoanDetails(String mobileNumber);

    Boolean updateLoanDetails(LoansDto loansDto);

    Boolean deleteLoan(String mobileNumber);
}
