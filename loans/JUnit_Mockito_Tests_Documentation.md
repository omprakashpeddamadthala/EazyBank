# JUnit and Mockito Tests Documentation for Loans Service

## Overview

This document provides an overview of the JUnit and Mockito tests implemented for the Loans service in the EazyBank application. The tests cover both the controller layer and the service layer, following professional coding standards and best practices.

## Test Structure

The tests are organized into two main classes:

1. `LoansControllerTest` - Tests for the controller layer
2. `LoansServiceImplTest` - Tests for the service layer

## Controller Layer Tests

The `LoansControllerTest` class tests the `LoansController` endpoints:

- `createLoan` - Tests creating a new loan
- `fetchLoanDetails` - Tests retrieving loan details
- `updateLoanDetails` - Tests updating loan details (both successful and unsuccessful scenarios)
- `deleteLoan` - Tests deleting a loan (both successful and unsuccessful scenarios)

### Best Practices Implemented

1. **Mocking Dependencies**: The `LoansService` dependency is mocked using Mockito's `@Mock` annotation.
2. **Dependency Injection**: The mocked service is injected into the controller using Mockito's `@InjectMocks` annotation.
3. **Test Data Setup**: Test data is set up in the `@BeforeEach` method to avoid duplication.
4. **Arrange-Act-Assert Pattern**: Each test follows the Arrange-Act-Assert pattern for clarity.
5. **Descriptive Test Names**: Test methods have descriptive names that indicate what is being tested and the expected outcome.
6. **Verification**: Each test verifies that the controller calls the service with the correct parameters and returns the expected response.
7. **Testing Edge Cases**: Tests cover both successful and unsuccessful scenarios.

## Service Layer Tests

The `LoansServiceImplTest` class tests the `LoansServiceImpl` methods:

- `createLoan` - Tests creating a new loan (both when a loan doesn't exist and when it already exists)
- `fetchLoanDetails` - Tests retrieving loan details (both when a loan exists and when it doesn't)
- `updateLoanDetails` - Tests updating loan details (both when a loan exists and when it doesn't)
- `deleteLoan` - Tests deleting a loan (both when a loan exists and when it doesn't)

### Best Practices Implemented

1. **Mocking Dependencies**: The `LoansRepository` dependency is mocked using Mockito's `@Mock` annotation.
2. **Dependency Injection**: The mocked repository is injected into the service using Mockito's `@InjectMocks` annotation.
3. **Test Data Setup**: Test data is set up in the `@BeforeEach` method to avoid duplication.
4. **Arrange-Act-Assert Pattern**: Each test follows the Arrange-Act-Assert pattern for clarity.
5. **Descriptive Test Names**: Test methods have descriptive names that indicate what is being tested and the expected outcome.
6. **Verification**: Each test verifies that the service calls the repository with the correct parameters and returns the expected result or throws the expected exception.
7. **Testing Edge Cases**: Tests cover both successful and error scenarios, including exception handling.
8. **Stubbing Method Calls**: Mockito's `when` and `doNothing` methods are used to stub repository method calls.

## Mockito Features Used

1. **@Mock**: Used to create mock objects of dependencies.
2. **@InjectMocks**: Used to inject mock dependencies into the class under test.
3. **when().thenReturn()**: Used to stub method calls and specify return values.
4. **doNothing().when()**: Used to stub void methods.
5. **verify()**: Used to verify that methods were called with the expected parameters.
6. **times()**: Used to verify the number of method invocations.
7. **never()**: Used to verify that a method was never called.
8. **any()**: Used as a parameter matcher for flexible argument matching.

## Testing Best Practices

1. **Isolation**: Each test is isolated and doesn't depend on the state of other tests.
2. **Readability**: Tests are written to be readable and maintainable.
3. **Coverage**: Tests cover all public methods and important edge cases.
4. **Assertions**: Appropriate assertions are used to verify the expected behavior.
5. **Mocking**: External dependencies are mocked to isolate the unit under test.
6. **Test Data**: Test data is created to be realistic but minimal.
7. **Test Names**: Test names are descriptive and follow a consistent naming convention.
8. **Setup and Teardown**: Common setup code is extracted to `@BeforeEach` methods.

## Bug Fixes

During the implementation of the tests, a bug was identified and fixed in the `LoansServiceImpl.updateLoanDetails()` method. The method was not assigning the result of `LoansMapper.mapToLoans(loansDto)` to the `loans` variable before saving it to the repository. 

Before the fix, the code was calling the mapper but not using its return value:
- LoansMapper.mapToLoans(loansDto);
- loansRepository.save(loans);

After the fix, the code properly assigns the mapped object to the loans variable:
- loans = LoansMapper.mapToLoans(loansDto);
- loansRepository.save(loans);

This fix ensures that the loan is properly updated with the values from the DTO before being saved to the repository.

## Conclusion

The implemented tests provide comprehensive coverage of the Loans service's controller and service layers. They follow professional coding standards and best practices, making them maintainable and reliable. The tests also helped identify and fix a bug in the service implementation, demonstrating the value of thorough testing.
