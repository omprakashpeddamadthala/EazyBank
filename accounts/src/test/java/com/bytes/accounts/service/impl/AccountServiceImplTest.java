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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private CustomerDto customerDto;
    private Customer customer;
    private Accounts accounts;

    @BeforeEach
    void setUp() {
        // Setup test data
        customerDto = CustomerDto.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .mobileNumber("9848149507")
                .accountsDto(AccountsDto.builder()
                        .accountNumber(1234567890L)
                        .accountType(AccountsConstants.SAVINGS)
                        .branchAddress(AccountsConstants.ADDRESS)
                        .build())
                .build();

        customer = Customer.builder()
                .customerId(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .mobileNumber("9848149507")
                .build();

        accounts = Accounts.builder()
                .accountNumber(1234567890L)
                .accountType(AccountsConstants.SAVINGS)
                .branchAddress(AccountsConstants.ADDRESS)
                .customerId(1L)
                .build();
    }

    @Test
    void createAccount_whenCustomerDoesNotExist_shouldCreateAccountSuccessfully() {
        // Arrange
        try (MockedStatic<CustomerMapper> customerMapperMockedStatic = mockStatic(CustomerMapper.class)) {
            customerMapperMockedStatic.when(() -> CustomerMapper.mapToCustomer(any(CustomerDto.class))).thenReturn(customer);

            when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());
            when(customerRepository.save(any(Customer.class))).thenReturn(customer);
            when(accountRepository.save(any(Accounts.class))).thenReturn(accounts);

            // Act
            accountService.createAccount(customerDto);

            // Assert
            verify(customerRepository, times(1)).findByMobileNumber(customer.getMobileNumber());
            verify(customerRepository, times(1)).save(any(Customer.class));
            verify(accountRepository, times(1)).save(any(Accounts.class));
        }
    }

    @Test
    void createAccount_whenCustomerExists_shouldThrowCustomerAlreadyExistException() {
        // Arrange
        try (MockedStatic<CustomerMapper> customerMapperMockedStatic = mockStatic(CustomerMapper.class)) {
            customerMapperMockedStatic.when(() -> CustomerMapper.mapToCustomer(any(CustomerDto.class))).thenReturn(customer);

            when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(customer));

            // Act & Assert
            assertThrows(CustomerAlreadyExistException.class, () -> accountService.createAccount(customerDto));

            // Verify
            verify(customerRepository, times(1)).findByMobileNumber(customer.getMobileNumber());
            verify(customerRepository, never()).save(any(Customer.class));
            verify(accountRepository, never()).save(any(Accounts.class));
        }
    }

    @Test
    void fetchAccountDetails_whenCustomerAndAccountExist_shouldReturnCustomerDto() {
        // Arrange
        try (MockedStatic<CustomerMapper> customerMapperMockedStatic = mockStatic(CustomerMapper.class);
             MockedStatic<AccountsMapper> accountsMapperMockedStatic = mockStatic(AccountsMapper.class)) {

            customerMapperMockedStatic.when(() -> CustomerMapper.mapToCustomerDto(any(Customer.class))).thenReturn(customerDto);
            accountsMapperMockedStatic.when(() -> AccountsMapper.mapToAccountDto(any(Accounts.class))).thenReturn(customerDto.getAccountsDto());

            when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(customer));
            when(accountRepository.findByCustomerId(anyLong())).thenReturn(Optional.of(accounts));

            // Act
            CustomerDto result = accountService.fetchAccountDetails("9848149507");

            // Assert
            assertNotNull(result);
            assertEquals(customerDto.getName(), result.getName());
            assertEquals(customerDto.getEmail(), result.getEmail());
            assertEquals(customerDto.getMobileNumber(), result.getMobileNumber());
            assertNotNull(result.getAccountsDto());

            // Verify
            verify(customerRepository, times(1)).findByMobileNumber("9848149507");
            verify(accountRepository, times(1)).findByCustomerId(customer.getCustomerId());
        }
    }

    @Test
    void fetchAccountDetails_whenCustomerDoesNotExist_shouldThrowResourceNotFoundException() {
        // Arrange
        when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> accountService.fetchAccountDetails("9848149507"));

        // Verify
        verify(customerRepository, times(1)).findByMobileNumber("9848149507");
        verify(accountRepository, never()).findByCustomerId(anyLong());
    }

    @Test
    void fetchAccountDetails_whenAccountDoesNotExist_shouldThrowResourceNotFoundException() {
        // Arrange
        when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomerId(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> accountService.fetchAccountDetails("9848149507"));

        // Verify
        verify(customerRepository, times(1)).findByMobileNumber("9848149507");
        verify(accountRepository, times(1)).findByCustomerId(customer.getCustomerId());
    }

    @Test
    void updateAccountDetails_whenAccountAndCustomerExist_shouldUpdateSuccessfully() {
        // Arrange
        AccountsDto accountsDto = customerDto.getAccountsDto();

        when(accountRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accounts));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Accounts.class))).thenReturn(accounts);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        Boolean result = accountService.updateAccountDetails(customerDto);

        // Assert
        assertTrue(result);

        // Verify
        verify(accountRepository, times(1)).findByAccountNumber(accountsDto.getAccountNumber());
        verify(customerRepository, times(1)).findById(accounts.getCustomerId());
        verify(accountRepository, times(1)).save(accounts);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateAccountDetails_whenAccountDoesNotExist_shouldThrowResourceNotFoundException() {
        // Arrange
        AccountsDto accountsDto = customerDto.getAccountsDto();

        when(accountRepository.findByAccountNumber(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> accountService.updateAccountDetails(customerDto));

        // Verify
        verify(accountRepository, times(1)).findByAccountNumber(accountsDto.getAccountNumber());
        verify(customerRepository, never()).findById(anyLong());
        verify(accountRepository, never()).save(any(Accounts.class));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateAccountDetails_whenCustomerDoesNotExist_shouldThrowResourceNotFoundException() {
        // Arrange
        AccountsDto accountsDto = customerDto.getAccountsDto();

        when(accountRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accounts));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> accountService.updateAccountDetails(customerDto));

        // Verify
        verify(accountRepository, times(1)).findByAccountNumber(accountsDto.getAccountNumber());
        verify(customerRepository, times(1)).findById(accounts.getCustomerId());
    }

    @Test
    void deleteAccount_whenCustomerExists_shouldDeleteSuccessfully() {
        // Arrange
        when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(customer));
        doNothing().when(accountRepository).deleteByCustomerId(anyLong());
        doNothing().when(customerRepository).deleteById(anyLong());

        // Act
        Boolean result = accountService.deleteAccount("9848149507");

        // Assert
        assertTrue(result);

        // Verify
        verify(customerRepository, times(1)).findByMobileNumber("9848149507");
        verify(accountRepository, times(1)).deleteByCustomerId(customer.getCustomerId());
        verify(customerRepository, times(1)).deleteById(customer.getCustomerId());
    }

    @Test
    void deleteAccount_whenCustomerDoesNotExist_shouldThrowResourceNotFoundException() {
        // Arrange
        when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount("9848149507"));

        // Verify
        verify(customerRepository, times(1)).findByMobileNumber("9848149507");
        verify(accountRepository, never()).deleteByCustomerId(anyLong());
        verify(customerRepository, never()).deleteById(anyLong());
    }
}
