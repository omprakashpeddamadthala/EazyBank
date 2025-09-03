package com.bytes.cards.service.impl;

import com.bytes.cards.constants.CardsConstants;
import com.bytes.cards.dto.CardsDto;
import com.bytes.cards.entity.Cards;
import com.bytes.cards.exception.CardAlreadyExistException;
import com.bytes.cards.exception.ResourceNotFoundException;
import com.bytes.cards.mapper.CardMapper;
import com.bytes.cards.repository.CardsRepository;
import com.bytes.cards.service.CardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardsServiceImpl implements CardsService {

    private final CardsRepository cardsRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards= cardsRepository.findByMobileNumber(mobileNumber);
        if(optionalCards.isPresent()){
            throw new CardAlreadyExistException("Card already registered with given mobileNumber "+mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType( CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        return CardMapper.mapToCardsDto( cards );
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        log.info( "Inside CardsServiceImpl.updateCard for mobile number: {}", cardsDto.getMobileNumber() );
        Cards cards = cardsRepository.findByMobileNumber( cardsDto.getMobileNumber() ).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", cardsDto.getMobileNumber()) );
        cards.setCardNumber(cardsDto.getCardNumber());
        cards.setCardType(cardsDto.getCardType());
        cards.setTotalLimit(cardsDto.getTotalLimit());
        cards.setAmountUsed(cardsDto.getAmountUsed());
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
