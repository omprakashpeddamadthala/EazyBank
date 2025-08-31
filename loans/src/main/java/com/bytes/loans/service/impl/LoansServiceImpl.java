package com.bytes.loans.service.impl;

import com.bytes.loans.constants.LoansConstants;
import com.bytes.loans.dto.LoansDto;
import com.bytes.loans.entity.Loans;
import com.bytes.loans.exception.LoanAlreadyExistException;
import com.bytes.loans.exception.ResourceNotFoundException;
import com.bytes.loans.mapper.LoansMapper;
import com.bytes.loans.repository.LoansRepository;
import com.bytes.loans.service.LoansService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoansServiceImpl implements LoansService {

    private final LoansRepository loansRepository;

    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> loans =loansRepository.findByMobileNumber( mobileNumber );
        if(loans.isPresent()){
            log.info("Loan already exists for mobile number: {}", mobileNumber);
            throw new LoanAlreadyExistException("Loan already exists for mobile number: " + mobileNumber);
        }
        loansRepository.save( createNewLoan( mobileNumber ));
    }

    private Loans createNewLoan(String mobileNumber) {
      return Loans.builder()
                .loanNumber( 9999999999999L + (long)(Math.random() * ((9999999999999L - 999999999999L) + 1)) + "" )
                .mobileNumber( mobileNumber )
                .loanType( LoansConstants.HOME_LOAN )
                .totalLoan( LoansConstants.NEW_LOAN_LIMIT )
                .amountPaid( 0 )
                .outstandingAmount( 80000 )
                .build();
    }

    @Override
    public LoansDto fetchLoanDetails(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        return LoansMapper.mapToLoanDto( loans );
    }

    @Override
    public Boolean updateLoanDetails(LoansDto loansDto) {
        Loans loans = loansRepository.findByMobileNumber(loansDto.getMobileNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", loansDto.getMobileNumber())
        );
        LoansMapper.mapToLoans( loansDto );
        loansRepository.save(loans);
        return  true;
    }

    @Override
    public Boolean deleteLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        loansRepository.deleteById( loans.getLoanId() );
        return true;
    }
}
