package com.bytes.cards.mapper;


import com.bytes.cards.dto.CardsDto;
import com.bytes.cards.entity.Cards;

public class CardMapper {

    public static Cards mapToCards(CardsDto cardsDto) {
        return Cards.builder()
                .mobileNumber(cardsDto.getMobileNumber())
                .cardNumber(cardsDto.getCardNumber())
                .cardType(cardsDto.getCardType())
                .totalLimit(cardsDto.getTotalLimit())
                .amountUsed(cardsDto.getAmountUsed())
                .availableAmount(cardsDto.getAvailableAmount())
                .build();
    }

    public static CardsDto mapToCardsDto(Cards cards) {
        return CardsDto.builder()
                .mobileNumber(cards.getMobileNumber())
                .cardNumber(cards.getCardNumber())
                .cardType(cards.getCardType())
                .totalLimit(cards.getTotalLimit())
                .amountUsed(cards.getAmountUsed())
                .availableAmount(cards.getAvailableAmount())
                .build();
    }
}
