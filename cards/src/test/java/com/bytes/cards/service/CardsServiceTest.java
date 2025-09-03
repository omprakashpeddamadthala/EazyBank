package com.bytes.cards.service;

import com.bytes.cards.dto.CardsDto;
import com.bytes.cards.entity.Cards;
import com.bytes.cards.exception.CardAlreadyExistException;
import com.bytes.cards.repository.CardsRepository;
import com.bytes.cards.service.impl.CardsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardsServiceTest {

    @Mock
    private CardsRepository cardsRepository;

    @InjectMocks
    private CardsServiceImpl cardsService;

    private Cards cards;
    private CardsDto cardsDto;
    private final String MOBILE_NUMBER = "1234567890";

    @BeforeEach
    void setUp() {
        // Setup test data
        cards = Cards.builder()
                .cardId(1L)
                .mobileNumber(MOBILE_NUMBER)
                .cardNumber("123456789012")
                .cardType("Credit Card")
                .totalLimit(100000)
                .amountUsed(1000)
                .availableAmount(99000)
                .build();

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
    void fetchCard_shouldReturnCardDetails_whenCardExists() {
        // Arrange
        when(cardsRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(cards));

        // Act
        CardsDto result = cardsService.fetchCard(MOBILE_NUMBER);

        // Assert
        assertNotNull(result);
        assertEquals(MOBILE_NUMBER, result.getMobileNumber());
        assertEquals(cards.getCardNumber(), result.getCardNumber());
        assertEquals(cards.getCardType(), result.getCardType());
        assertEquals(cards.getTotalLimit(), result.getTotalLimit());
        assertEquals(cards.getAmountUsed(), result.getAmountUsed());
        assertEquals(cards.getAvailableAmount(), result.getAvailableAmount());

        // Verify
        verify(cardsRepository, times(1)).findByMobileNumber(MOBILE_NUMBER);
    }

    @Test
    void fetchCard_shouldThrowException_whenCardDoesNotExist() {
        // Arrange
        when(cardsRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CardAlreadyExistException.class, () -> cardsService.fetchCard(MOBILE_NUMBER));

        // Verify
        verify(cardsRepository, times(1)).findByMobileNumber(MOBILE_NUMBER);
    }

    @Test
    void updateCard_shouldUpdateAndReturnTrue_whenCardExists() {
        // Arrange
        when(cardsRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(cards));
        when(cardsRepository.save(any(Cards.class))).thenReturn(cards);

        // Act
        boolean result = cardsService.updateCard(cardsDto);

        // Assert
        assertTrue(result);

        // Verify
        verify(cardsRepository, times(1)).findByMobileNumber(MOBILE_NUMBER);
        verify(cardsRepository, times(1)).save(any(Cards.class));
    }

    @Test
    void updateCard_shouldThrowException_whenCardDoesNotExist() {
        // Arrange
        when(cardsRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CardAlreadyExistException.class, () -> cardsService.updateCard(cardsDto));

        // Verify
        verify(cardsRepository, times(1)).findByMobileNumber(MOBILE_NUMBER);
        verify(cardsRepository, never()).save(any(Cards.class));
    }

    @Test
    void deleteCard_shouldDeleteAndReturnTrue_whenCardExists() {
        // Arrange
        when(cardsRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(cards));
        doNothing().when(cardsRepository).deleteById(anyLong());

        // Act
        boolean result = cardsService.deleteCard(MOBILE_NUMBER);

        // Assert
        assertTrue(result);

        // Verify
        verify(cardsRepository, times(1)).findByMobileNumber(MOBILE_NUMBER);
        verify(cardsRepository, times(1)).deleteById(cards.getCardId());
    }

    @Test
    void deleteCard_shouldThrowException_whenCardDoesNotExist() {
        // Arrange
        when(cardsRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CardAlreadyExistException.class, () -> cardsService.deleteCard(MOBILE_NUMBER));

        // Verify
        verify(cardsRepository, times(1)).findByMobileNumber(MOBILE_NUMBER);
        verify(cardsRepository, never()).deleteById(anyLong());
    }
}
