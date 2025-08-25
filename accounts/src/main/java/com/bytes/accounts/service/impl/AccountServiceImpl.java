package com.bytes.accounts.service.impl;

import com.bytes.accounts.constants.AccountsConstants;
import com.bytes.accounts.dto.AccountsDto;
import com.bytes.accounts.dto.CustomerDto;
import com.bytes.accounts.entity.Accounts;
import com.bytes.accounts.entity.Customer;
import com.bytes.accounts.exception.CustomerAlreadyExistException;
import com.bytes.accounts.exception.ResourceNotFoundException;
import com.bytes.accounts.mapper.AccountsMapper;
import com.bytes.accounts.mapper.CustomerMapper;
import com.bytes.accounts.repository.AccountRepository;
import com.bytes.accounts.repository.CustomerRepository;
import com.bytes.accounts.service.AccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountsService {

    private AccountRepository accountRepository;

    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {

        Customer customer = CustomerMapper.mapToCustomer( customerDto );
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber( customer.getMobileNumber() );

        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistException( "Customer with mobile number " + customer.getMobileNumber() + " already exists" );
        }

        Customer savedCustomer = customerRepository.save( customer );
        accountRepository.save( createNewAccount( savedCustomer ) );

    }

    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber( mobileNumber ).orElseThrow(
                () -> {
                    throw new ResourceNotFoundException( "Customer ", "mobileNumber", mobileNumber );
                }
        );

        Accounts Accounts = accountRepository.findByCustomerId( customer.getCustomerId() ).orElseThrow(
                () -> {
                    throw new ResourceNotFoundException( "Accounts ", "customerId", customer.getCustomerId() );
                }
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto( customer );
        customerDto.setAccountsDto( AccountsMapper.mapToAccountDto( Accounts ) );
        return customerDto;
    }

    @Override
    public Boolean updateAccountDetails(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {

            Long accountNumber = accountsDto.getAccountNumber();
            Accounts Accounts = accountRepository.findByAccountNumber( accountNumber ).orElseThrow(
                    () -> {
                        throw new ResourceNotFoundException( "Accounts ", "accountNumber", accountNumber );
                    }
            );
            Accounts.setAccountType( accountsDto.getAccountType() );
            Accounts.setBranchAddress( accountsDto.getBranchAddress() );
            accountRepository.save( Accounts );

            Long customerId = Accounts.getCustomerId();
            Customer customer = customerRepository.findById( customerId ).orElseThrow(
                    () -> {
                        throw new ResourceNotFoundException( "Customer ", "customerId", customerId );
                    }
            );
            customer.setName( customerDto.getName() );
            customer.setEmail( customerDto.getEmail() );
            customer.setMobileNumber( customerDto.getMobileNumber() );
            customerRepository.save( customer );
            isUpdated = true;
        }

        return isUpdated;
    }

    @Override
    public Boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber( mobileNumber ).orElseThrow(
                () -> {
                    throw new ResourceNotFoundException( "Customer ", "mobileNumber", mobileNumber );
                }
        );

        accountRepository.deleteByCustomerId( customer.getCustomerId() );
        customerRepository.deleteById( customer.getCustomerId() );
        return true;
    }

    private Accounts createNewAccount(Customer savedCustomer) {
        Accounts accounts = Accounts.builder()
                .customerId( savedCustomer.getCustomerId() )
                .accountNumber( 100000000L + (long) (Math.random() * 899999999L) )
                .accountType( AccountsConstants.SAVINGS )
                .branchAddress( AccountsConstants.ADDRESS )
                .build();
        return accounts;
    }

}
