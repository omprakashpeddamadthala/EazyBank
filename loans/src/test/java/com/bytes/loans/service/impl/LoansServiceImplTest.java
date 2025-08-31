package com.bytes.loans.service.impl;

import com.bytes.loans.constants.LoansConstants;
import com.bytes.loans.dto.LoansDto;
import com.bytes.loans.entity.Loans;
import com.bytes.loans.exception.LoanAlreadyExistException;
import com.bytes.loans.exception.ResourceNotFoundException;
import com.bytes.loans.repository.LoansRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoansServiceImplTest {

    @Mock
    private LoansRepository loansRepository;

    @InjectMocks
    private LoansServiceImpl loansService;

    private String mobileNumber;
    private Loans loans;
    private LoansDto loansDto;

    @BeforeEach
    void setUp() {
        mobileNumber = "1234567890";
        loans = Loans.builder()
                .loanId(1L)
                .mobileNumber(mobileNumber)
                .loanNumber("123456789012")
                .loanType(LoansConstants.HOME_LOAN)
                .totalLoan(100000)
                .amountPaid(20000)
                .outstandingAmount(80000)
                .build();

        loansDto = LoansDto.builder()
                .mobileNumber(mobileNumber)
                .loanNumber("123456789012")
                .loanType(LoansConstants.HOME_LOAN)
                .totalLoan(100000)
                .amountPaid(20000)
                .outstandingAmount(80000)
                .build();
    }

    @Test
    void createLoan_WhenLoanDoesNotExist_ShouldCreateLoan() {
        // Arrange
        when(loansRepository.findByMobileNumber(mobileNumber)).thenReturn(Optional.empty());
        when(loansRepository.save(any(Loans.class))).thenReturn(loans);

        // Act
        loansService.createLoan(mobileNumber);

        // Assert
        verify(loansRepository, times(1)).findByMobileNumber(mobileNumber);
        verify(loansRepository, times(1)).save(any(Loans.class));
    }

    @Test
    void createLoan_WhenLoanExists_ShouldThrowException() {
        // Arrange
        when(loansRepository.findByMobileNumber(mobileNumber)).thenReturn(Optional.of(loans));

        // Act & Assert
        assertThrows(LoanAlreadyExistException.class, () -> loansService.createLoan(mobileNumber));
        verify(loansRepository, times(1)).findByMobileNumber(mobileNumber);
        verify(loansRepository, never()).save(any(Loans.class));
    }

    @Test
    void fetchLoanDetails_WhenLoanExists_ShouldReturnLoanDetails() {
        // Arrange
        when(loansRepository.findByMobileNumber(mobileNumber)).thenReturn(Optional.of(loans));

        // Act
        LoansDto result = loansService.fetchLoanDetails(mobileNumber);

        // Assert
        verify(loansRepository, times(1)).findByMobileNumber(mobileNumber);
        assertEquals(mobileNumber, result.getMobileNumber());
        assertEquals(loans.getLoanNumber(), result.getLoanNumber());
        assertEquals(loans.getLoanType(), result.getLoanType());
        assertEquals(loans.getTotalLoan(), result.getTotalLoan());
        assertEquals(loans.getAmountPaid(), result.getAmountPaid());
        assertEquals(loans.getOutstandingAmount(), result.getOutstandingAmount());
    }

    @Test
    void fetchLoanDetails_WhenLoanDoesNotExist_ShouldThrowException() {
        // Arrange
        when(loansRepository.findByMobileNumber(mobileNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> loansService.fetchLoanDetails(mobileNumber));
        verify(loansRepository, times(1)).findByMobileNumber(mobileNumber);
    }

    @Test
    void updateLoanDetails_WhenLoanExists_ShouldUpdateAndReturnTrue() {
        // Arrange
        when(loansRepository.findByMobileNumber(mobileNumber)).thenReturn(Optional.of(loans));
        when(loansRepository.save(any(Loans.class))).thenReturn(loans);

        // Act
        boolean result = loansService.updateLoanDetails(loansDto);

        // Assert
        verify(loansRepository, times(1)).findByMobileNumber(mobileNumber);
        verify(loansRepository, times(1)).save(any(Loans.class));
        assertTrue(result);
    }

    @Test
    void updateLoanDetails_WhenLoanDoesNotExist_ShouldThrowException() {
        // Arrange
        when(loansRepository.findByMobileNumber(mobileNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> loansService.updateLoanDetails(loansDto));
        verify(loansRepository, times(1)).findByMobileNumber(mobileNumber);
        verify(loansRepository, never()).save(any(Loans.class));
    }

    @Test
    void deleteLoan_WhenLoanExists_ShouldDeleteAndReturnTrue() {
        // Arrange
        when(loansRepository.findByMobileNumber(mobileNumber)).thenReturn(Optional.of(loans));
        doNothing().when(loansRepository).deleteById(loans.getLoanId());

        // Act
        boolean result = loansService.deleteLoan(mobileNumber);

        // Assert
        verify(loansRepository, times(1)).findByMobileNumber(mobileNumber);
        verify(loansRepository, times(1)).deleteById(loans.getLoanId());
        assertTrue(result);
    }

    @Test
    void deleteLoan_WhenLoanDoesNotExist_ShouldThrowException() {
        // Arrange
        when(loansRepository.findByMobileNumber(mobileNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> loansService.deleteLoan(mobileNumber));
        verify(loansRepository, times(1)).findByMobileNumber(mobileNumber);
        verify(loansRepository, never()).deleteById(any());
    }
}