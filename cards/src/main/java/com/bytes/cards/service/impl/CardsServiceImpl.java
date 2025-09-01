package com.bytes.cards.service.impl;

import com.bytes.cards.dto.CardsDto;
import com.bytes.cards.entity.Cards;
import com.bytes.cards.exception.CardAlreadyExistException;
import com.bytes.cards.mapper.CardMapper;
import com.bytes.cards.repository.CardsRepository;
import com.bytes.cards.service.CardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardsServiceImpl implements CardsService {

    private final CardsRepository cardsRepository;

    @Override
    public void createCard(String mobileNumber) {
        log.info( "Inside CardsServiceImpl.createCard for mobile number: {}", mobileNumber );
        Cards cards = cardsRepository.findByMobileNumber( mobileNumber ).orElseThrow(
                () -> new CardAlreadyExistException( "Card already exists for mobile number: " + mobileNumber ) );

    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        log.info( "Inside CardsServiceImpl.fetchCard for mobile number: {}", mobileNumber );
        Cards cards = cardsRepository.findByMobileNumber( mobileNumber ).orElseThrow(
                () -> new CardAlreadyExistException( "Card already exists for mobile number: " + mobileNumber ) );
        return CardMapper.mapToCardsDto( cards );
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        log.info( "Inside CardsServiceImpl.updateCard for mobile number: {}", cardsDto.getMobileNumber() );
        Cards cards = cardsRepository.findByMobileNumber( cardsDto.getMobileNumber() ).orElseThrow(
                () -> new CardAlreadyExistException( "Card already exists for mobile number: " + cardsDto.getMobileNumber() ) );
        cards.setAmountUsed( cardsDto.getAmountUsed() );
        cards.setAvailableAmount( cardsDto.getAvailableAmount() );
        cardsRepository.save( cards );
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        log.info( "Inside CardsServiceImpl.deleteCard for mobile number: {}", mobileNumber );
        Cards cards = cardsRepository.findByMobileNumber( mobileNumber ).orElseThrow(
                () -> new CardAlreadyExistException( "Card already exists for mobile number: " + mobileNumber ) );
        cardsRepository.deleteById( cards.getCardId() );
        return true;
    }
}
