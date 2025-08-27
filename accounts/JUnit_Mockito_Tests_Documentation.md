# JUnit and Mockito Tests Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [Testing Framework Overview](#testing-framework-overview)
3. [Controller Layer Tests](#controller-layer-tests)
4. [Service Layer Tests](#service-layer-tests)
5. [Testing Patterns and Best Practices](#testing-patterns-and-best-practices)
6. [Conclusion](#conclusion)

## Introduction

This document provides a detailed explanation of the JUnit and Mockito tests implemented for the EazyBank Accounts microservice. The tests cover both the controller and service layers, ensuring that all components work as expected.

## Testing Framework Overview

### JUnit 5
JUnit 5 is the latest version of the popular testing framework for Java applications. It provides annotations and assertions to create and run tests.

Key features used:
- `@Test`: Marks a method as a test method
- `@BeforeEach`: Executes a method before each test
- `@ExtendWith`: Integrates with other frameworks like Mockito

### Mockito
Mockito is a mocking framework that allows the creation of test doubles (mocks) for dependencies. This enables isolated testing of components without relying on their actual implementations.

Key features used:
- `@Mock`: Creates a mock object
- `@InjectMocks`: Injects mock objects into the class under test
- `when()`: Defines behavior for mock objects
- `verify()`: Verifies that specific methods were called on mock objects

## Controller Layer Tests

The `AccountsControllerTest` class tests the REST API endpoints in the `AccountsController`. It uses Spring's `MockMvc` to simulate HTTP requests and verify responses.

### Test Setup

```java
@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(accountsController).build();
}
```

This setup initializes the mocks and creates a `MockMvc` instance for testing HTTP endpoints.

### Key Tests

#### 1. Create Account Test

```java
@Test
void createAccount_shouldReturnCreatedStatus() throws Exception {
    // Arrange
    CustomerDto customerDto = createCustomerDto();
    doNothing().when(accountsService).createAccount(any(CustomerDto.class));

    // Act & Assert
    mockMvc.perform(post("/api/v1/accounts/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value(AccountsConstants.STATUS_201))
            .andExpect(jsonPath("$.statusMessage").value(AccountsConstants.MESSAGE_201));

    // Verify
    verify(accountsService, times(1)).createAccount(any(CustomerDto.class));
}
```

This test:
1. Creates a test customer DTO
2. Mocks the service layer to do nothing when `createAccount` is called
3. Performs a POST request to the create endpoint
4. Verifies that the response has a 201 Created status
5. Checks that the response body contains the expected status code and message
6. Verifies that the service method was called exactly once

#### 2. Fetch Account Details Test

```java
@Test
void fetchAccountDetails_shouldReturnCustomerDetails() throws Exception {
    // Arrange
    CustomerDto customerDto = createCustomerDto();
    when(accountsService.fetchAccountDetails(anyString())).thenReturn(customerDto);

    // Act & Assert
    mockMvc.perform(get("/api/v1/accounts/fetch")
            .param("mobileNumber", "9848149507"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(customerDto.getName()))
            .andExpect(jsonPath("$.email").value(customerDto.getEmail()))
            .andExpect(jsonPath("$.mobileNumber").value(customerDto.getMobileNumber()))
            .andExpect(jsonPath("$.accountsDto.accountNumber").value(customerDto.getAccountsDto().getAccountNumber()))
            .andExpect(jsonPath("$.accountsDto.accountType").value(customerDto.getAccountsDto().getAccountType()))
            .andExpect(jsonPath("$.accountsDto.branchAddress").value(customerDto.getAccountsDto().getBranchAddress()));

    // Verify
    verify(accountsService, times(1)).fetchAccountDetails("9848149507");
}
```

This test:
1. Creates a test customer DTO
2. Mocks the service layer to return the customer DTO when `fetchAccountDetails` is called
3. Performs a GET request to the fetch endpoint with a mobile number parameter
4. Verifies that the response has a 200 OK status
5. Checks that the response body contains the expected customer details
6. Verifies that the service method was called exactly once with the correct parameter

#### 3. Update Account Tests

Two tests are implemented for the update endpoint:
- `updateAccountDetails_whenSuccessful_shouldReturnOkStatus`: Tests the successful update scenario
- `updateAccountDetails_whenFailed_shouldReturnInternalServerError`: Tests the failure scenario

#### 4. Delete Account Tests

Two tests are implemented for the delete endpoint:
- `deleteAccount_whenSuccessful_shouldReturnOkStatus`: Tests the successful delete scenario
- `deleteAccount_whenFailed_shouldReturnExpectationFailedStatus`: Tests the failure scenario

## Service Layer Tests

The `AccountServiceImplTest` class tests the business logic in the `AccountServiceImpl` class. It uses Mockito to mock the repository layer.

### Test Setup

```java
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
}
```

This setup:
1. Uses `@ExtendWith(MockitoExtension.class)` to integrate JUnit with Mockito
2. Creates mock objects for the repositories
3. Injects the mocks into the service
4. Sets up test data in the `setUp` method

### Key Tests

#### 1. Create Account Tests

Two tests are implemented for the create account functionality:
- `createAccount_whenCustomerDoesNotExist_shouldCreateAccountSuccessfully`: Tests the successful creation scenario
- `createAccount_whenCustomerExists_shouldThrowCustomerAlreadyExistException`: Tests the scenario where the customer already exists

```java
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
```

This test:
1. Mocks the static `CustomerMapper` class to return a customer entity
2. Mocks the repository to return an empty optional when searching for a customer
3. Mocks the repositories to return the customer and account when saving
4. Calls the service method
5. Verifies that the repository methods were called with the correct parameters

#### 2. Fetch Account Details Tests

Three tests are implemented for the fetch account details functionality:
- `fetchAccountDetails_whenCustomerAndAccountExist_shouldReturnCustomerDto`: Tests the successful fetch scenario
- `fetchAccountDetails_whenCustomerDoesNotExist_shouldThrowResourceNotFoundException`: Tests the scenario where the customer doesn't exist
- `fetchAccountDetails_whenAccountDoesNotExist_shouldThrowResourceNotFoundException`: Tests the scenario where the account doesn't exist

#### 3. Update Account Details Tests

Three tests are implemented for the update account details functionality:
- `updateAccountDetails_whenAccountAndCustomerExist_shouldUpdateSuccessfully`: Tests the successful update scenario
- `updateAccountDetails_whenAccountDoesNotExist_shouldThrowResourceNotFoundException`: Tests the scenario where the account doesn't exist
- `updateAccountDetails_whenCustomerDoesNotExist_shouldThrowResourceNotFoundException`: Tests the scenario where the customer doesn't exist

#### 4. Delete Account Tests

Two tests are implemented for the delete account functionality:
- `deleteAccount_whenCustomerExists_shouldDeleteSuccessfully`: Tests the successful delete scenario
- `deleteAccount_whenCustomerDoesNotExist_shouldThrowResourceNotFoundException`: Tests the scenario where the customer doesn't exist

## Testing Patterns and Best Practices

### 1. Arrange-Act-Assert (AAA) Pattern

All tests follow the AAA pattern:
- **Arrange**: Set up the test data and mock behavior
- **Act**: Call the method being tested
- **Assert**: Verify the results and interactions

Example:
```java
// Arrange
CustomerDto customerDto = createCustomerDto();
when(accountsService.fetchAccountDetails(anyString())).thenReturn(customerDto);

// Act & Assert
mockMvc.perform(get("/api/v1/accounts/fetch")
        .param("mobileNumber", "9848149507"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(customerDto.getName()));

// Verify
verify(accountsService, times(1)).fetchAccountDetails("9848149507");
```

### 2. Mocking Dependencies

Dependencies are mocked to isolate the component being tested:
- Controller tests mock the service layer
- Service tests mock the repository layer and static mapper methods

### 3. Testing Edge Cases

Tests cover both happy paths and edge cases:
- Successful operations
- Failed operations
- Resource not found scenarios
- Duplicate resource scenarios

### 4. Clear Test Names

Test method names clearly describe what is being tested and the expected outcome:
```java
void updateAccountDetails_whenFailed_shouldReturnInternalServerError()
```

This name indicates:
- The method being tested: `updateAccountDetails`
- The scenario: `whenFailed`
- The expected outcome: `shouldReturnInternalServerError`

### 5. Test Data Setup

Test data is set up in a centralized way to avoid duplication:
```java
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
    // ...
}
```

### 6. Verification of Interactions

Tests verify not only the results but also the interactions with dependencies:
```java
verify(accountsService, times(1)).createAccount(any(CustomerDto.class));
```

## Conclusion

The JUnit and Mockito tests implemented for the EazyBank Accounts microservice provide comprehensive coverage of both the controller and service layers. The tests follow best practices and patterns to ensure that the code is thoroughly tested and behaves as expected.

Key benefits of the implemented tests:
1. **Isolation**: Components are tested in isolation from their dependencies
2. **Comprehensive Coverage**: All methods and edge cases are covered
3. **Clear Documentation**: Tests serve as documentation for how the code should behave
4. **Regression Prevention**: Tests help prevent regressions when code is modified