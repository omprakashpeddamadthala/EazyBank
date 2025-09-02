package com.bytes.cards.controller;

import com.bytes.cards.constants.CardsConstants;
import com.bytes.cards.dto.CardsDto;
import com.bytes.cards.dto.ResponseDto;
import com.bytes.cards.service.CardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardsControllerTest {

    @Mock
    private CardsService cardsService;

    @InjectMocks
    private CardsController cardsController;

    private CardsDto cardsDto;
    private final String MOBILE_NUMBER = "1234567890";

    @BeforeEach
    void setUp() {
        // Setup test data
        cardsDto = CardsDto.builder()
                .mobileNumber(MOBILE_NUMBER)
                .cardNumber("123456789012")
                .cardType("Credit Card")
                .totalLimit(100000)
                .amountUsed(1000)
                .availableAmount(99000)
                .build();
    }

    @Test
    void createCard_shouldReturnCreatedStatus() {
        // Arrange
        doNothing().when(cardsService).createCard(anyString());

        // Act
        ResponseEntity<ResponseDto> response = cardsController.createCard(MOBILE_NUMBER);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(CardsConstants.STATUS_201, response.getBody().getStatusCode());
        assertEquals(CardsConstants.MESSAGE_201, response.getBody().getStatusMessage());

        // Verify
        verify(cardsService, times(1)).createCard(MOBILE_NUMBER);
    }

    @Test
    void fetchCardDetails_shouldReturnCardDetails() {
        // Arrange
        when(cardsService.fetchCard(anyString())).thenReturn(cardsDto);

        // Act
        ResponseEntity<CardsDto> response = cardsController.fetchCardDetails(MOBILE_NUMBER);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cardsDto, response.getBody());

        // Verify
        verify(cardsService, times(1)).fetchCard(MOBILE_NUMBER);
    }

    @Test
    void updateCardDetails_shouldReturnSuccessResponse_whenUpdateIsSuccessful() {
        // Arrange
        when(cardsService.updateCard(any(CardsDto.class))).thenReturn(true);

        // Act
        ResponseEntity<ResponseDto> response = cardsController.updateCardDetails(cardsDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CardsConstants.STATUS_200, response.getBody().getStatusCode());
        assertEquals(CardsConstants.MESSAGE_200, response.getBody().getStatusMessage());

        // Verify
        verify(cardsService, times(1)).updateCard(cardsDto);
    }

    @Test
    void updateCardDetails_shouldReturnFailureResponse_whenUpdateFails() {
        // Arrange
        when(cardsService.updateCard(any(CardsDto.class))).thenReturn(false);

        // Act
        ResponseEntity<ResponseDto> response = cardsController.updateCardDetails(cardsDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        assertEquals(CardsConstants.STATUS_417, response.getBody().getStatusCode());
        assertEquals(CardsConstants.MESSAGE_417_UPDATE, response.getBody().getStatusMessage());

        // Verify
        verify(cardsService, times(1)).updateCard(cardsDto);
    }

    @Test
    void deleteCardDetails_shouldReturnSuccessResponse_whenDeleteIsSuccessful() {
        // Arrange
        when(cardsService.deleteCard(anyString())).thenReturn(true);

        // Act
        ResponseEntity<?> response = cardsController.deleteCardDetails(MOBILE_NUMBER);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseDto responseDto = (ResponseDto) response.getBody();
        assertEquals(CardsConstants.STATUS_200, responseDto.getStatusCode());
        assertEquals(CardsConstants.MESSAGE_200, responseDto.getStatusMessage());

        // Verify
        verify(cardsService, times(1)).deleteCard(MOBILE_NUMBER);
    }

    @Test
    void deleteCardDetails_shouldReturnFailureResponse_whenDeleteFails() {
        // Arrange
        when(cardsService.deleteCard(anyString())).thenReturn(false);

        // Act
        ResponseEntity<?> response = cardsController.deleteCardDetails(MOBILE_NUMBER);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        ResponseDto responseDto = (ResponseDto) response.getBody();
        assertEquals(CardsConstants.STATUS_417, responseDto.getStatusCode());
        assertEquals(CardsConstants.MESSAGE_417_DELETE, responseDto.getStatusMessage());

        // Verify
        verify(cardsService, times(1)).deleteCard(MOBILE_NUMBER);
    }
}
