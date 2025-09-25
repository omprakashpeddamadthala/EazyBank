package com.bytes.accounts.service.impl;

import com.bytes.accounts.dto.CardsDto;
import com.bytes.accounts.dto.CustomerDetailsDto;
import com.bytes.accounts.dto.CustomerDto;
import com.bytes.accounts.dto.LoansDto;
import com.bytes.accounts.entity.Accounts;
import com.bytes.accounts.entity.Customer;
import com.bytes.accounts.exception.ResourceNotFoundException;
import com.bytes.accounts.mapper.AccountsMapper;
import com.bytes.accounts.mapper.CustomerMapper;
import com.bytes.accounts.repository.AccountRepository;
import com.bytes.accounts.repository.CustomerRepository;
import com.bytes.accounts.service.CustomerService;
import com.bytes.accounts.service.client.CardsFeignClient;
import com.bytes.accounts.service.client.LoansFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    private final LoansFeignClient loansFeignClient;

    private final CardsFeignClient cardsFeignClient;

    @Override
    public CustomerDetailsDto getCustomerDetails(String mobileNumber,String correlationId) {
        log.info("Fetching customer details for mobile number: {}", mobileNumber);

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer ", "mobileNumber", mobileNumber)
        );

        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Accounts ", "customerId", customer.getCustomerId())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer);
        customerDetailsDto.setAccountsDto( AccountsMapper.mapToAccountDto( accounts ) );

        ResponseEntity<LoansDto> loansDto = loansFeignClient.fetchLoanDetails( mobileNumber, correlationId );
        ResponseEntity<CardsDto> cardDto = cardsFeignClient.fetchCardDetails( mobileNumber,correlationId );

        customerDetailsDto.setCardsDto(  cardDto.getBody() );
        customerDetailsDto.setLoansDto( loansDto.getBody() );
        return  customerDetailsDto;
    }
}
