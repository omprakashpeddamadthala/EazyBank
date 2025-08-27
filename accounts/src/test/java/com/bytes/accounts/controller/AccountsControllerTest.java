package com.bytes.accounts.controller;

import com.bytes.accounts.constants.AccountsConstants;
import com.bytes.accounts.dto.AccountsDto;
import com.bytes.accounts.dto.CustomerDto;
import com.bytes.accounts.dto.ResponseDto;
import com.bytes.accounts.service.AccountsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountsService accountsService;

    @InjectMocks
    private AccountsController accountsController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountsController).build();
    }

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

    @Test
    void updateAccountDetails_whenSuccessful_shouldReturnOkStatus() throws Exception {
        // Arrange
        CustomerDto customerDto = createCustomerDto();
        when(accountsService.updateAccountDetails(any(CustomerDto.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/api/v1/accounts/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(AccountsConstants.STATUS_200))
                .andExpect(jsonPath("$.statusMessage").value(AccountsConstants.MESSAGE_200));

        // Verify
        verify(accountsService, times(1)).updateAccountDetails(any(CustomerDto.class));
    }

    @Test
    void updateAccountDetails_whenFailed_shouldReturnInternalServerError() throws Exception {
        // Arrange
        CustomerDto customerDto = createCustomerDto();
        when(accountsService.updateAccountDetails(any(CustomerDto.class))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/api/v1/accounts/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.statusCode").value(AccountsConstants.STATUS_500))
                .andExpect(jsonPath("$.statusMessage").value(AccountsConstants.MESSAGE_500));

        // Verify
        verify(accountsService, times(1)).updateAccountDetails(any(CustomerDto.class));
    }

    @Test
    void deleteAccount_whenSuccessful_shouldReturnOkStatus() throws Exception {
        // Arrange
        when(accountsService.deleteAccount(anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/accounts/delete")
                .param("mobileNumber", "9848149507"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(AccountsConstants.STATUS_200))
                .andExpect(jsonPath("$.statusMessage").value(AccountsConstants.MESSAGE_200));

        // Verify
        verify(accountsService, times(1)).deleteAccount("9848149507");
    }

    @Test
    void deleteAccount_whenFailed_shouldReturnExpectationFailedStatus() throws Exception {
        // Arrange
        when(accountsService.deleteAccount(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/accounts/delete")
                .param("mobileNumber", "9848149507"))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.statusCode").value(AccountsConstants.STATUS_417))
                .andExpect(jsonPath("$.statusMessage").value(AccountsConstants.MESSAGE_417_DELETE));

        // Verify
        verify(accountsService, times(1)).deleteAccount("9848149507");
    }

    private CustomerDto createCustomerDto() {
        AccountsDto accountsDto = AccountsDto.builder()
                .accountNumber(1234567890L)
                .accountType("Savings")
                .branchAddress("123 Main Street, New York")
                .build();

        return CustomerDto.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .mobileNumber("9848149507")
                .accountsDto(accountsDto)
                .build();
    }
}