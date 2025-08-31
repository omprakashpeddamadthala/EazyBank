package com.bytes.loans.controller;

import com.bytes.loans.constants.LoansConstants;
import com.bytes.loans.dto.LoansDto;
import com.bytes.loans.dto.ResponseDto;
import com.bytes.loans.service.LoansService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoansControllerTest {

    @Mock
    private LoansService loansService;

    @InjectMocks
    private LoansController loansController;

    private LoansDto loansDto;
    private String mobileNumber;

    @BeforeEach
    void setUp() {
        mobileNumber = "1234567890";
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
    void createLoan_ShouldReturnCreatedStatus() {
        // Arrange
        doNothing().when(loansService).createLoan(mobileNumber);

        // Act
        ResponseEntity<ResponseDto> response = loansController.createLoan(mobileNumber);

        // Assert
        verify(loansService, times(1)).createLoan(mobileNumber);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(LoansConstants.STATUS_201, response.getBody().getStatusCode());
        assertEquals(LoansConstants.MESSAGE_201, response.getBody().getStatusMessage());
    }

    @Test
    void fetchLoanDetails_ShouldReturnLoanDetails() {
        // Arrange
        when(loansService.fetchLoanDetails(mobileNumber)).thenReturn(loansDto);

        // Act
        ResponseEntity<LoansDto> response = loansController.fetchLoanDetails(mobileNumber);

        // Assert
        verify(loansService, times(1)).fetchLoanDetails(mobileNumber);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loansDto, response.getBody());
    }

    @Test
    void updateLoanDetails_WhenSuccessful_ShouldReturnOkStatus() {
        // Arrange
        when(loansService.updateLoanDetails(loansDto)).thenReturn(true);

        // Act
        ResponseEntity<ResponseDto> response = loansController.updateLoanDetails(loansDto);

        // Assert
        verify(loansService, times(1)).updateLoanDetails(loansDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(LoansConstants.STATUS_200, response.getBody().getStatusCode());
        assertEquals(LoansConstants.MESSAGE_200, response.getBody().getStatusMessage());
    }

    @Test
    void updateLoanDetails_WhenUnsuccessful_ShouldReturnExpectationFailedStatus() {
        // Arrange
        when(loansService.updateLoanDetails(loansDto)).thenReturn(false);

        // Act
        ResponseEntity<ResponseDto> response = loansController.updateLoanDetails(loansDto);

        // Assert
        verify(loansService, times(1)).updateLoanDetails(loansDto);
        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        assertEquals(LoansConstants.STATUS_417, response.getBody().getStatusCode());
        assertEquals(LoansConstants.MESSAGE_417_UPDATE, response.getBody().getStatusMessage());
    }

    @Test
    void deleteLoan_WhenSuccessful_ShouldReturnOkStatus() {
        // Arrange
        when(loansService.deleteLoan(mobileNumber)).thenReturn(true);

        // Act
        ResponseEntity<?> response = loansController.deleteLoan(mobileNumber);

        // Assert
        verify(loansService, times(1)).deleteLoan(mobileNumber);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseDto responseDto = (ResponseDto) response.getBody();
        assertEquals(LoansConstants.STATUS_200, responseDto.getStatusCode());
        assertEquals(LoansConstants.MESSAGE_200, responseDto.getStatusMessage());
    }

    @Test
    void deleteLoan_WhenUnsuccessful_ShouldReturnExpectationFailedStatus() {
        // Arrange
        when(loansService.deleteLoan(mobileNumber)).thenReturn(false);

        // Act
        ResponseEntity<?> response = loansController.deleteLoan(mobileNumber);

        // Assert
        verify(loansService, times(1)).deleteLoan(mobileNumber);
        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        ResponseDto responseDto = (ResponseDto) response.getBody();
        assertEquals(LoansConstants.STATUS_417, responseDto.getStatusCode());
        assertEquals(LoansConstants.MESSAGE_417_DELETE, responseDto.getStatusMessage());
    }
}