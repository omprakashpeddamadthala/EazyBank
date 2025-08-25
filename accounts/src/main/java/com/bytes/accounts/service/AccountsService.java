package com.bytes.accounts.service;

import com.bytes.accounts.dto.CustomerDto;

public interface AccountsService {

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccountDetails(String mobileNumber);

    Boolean updateAccountDetails(CustomerDto customerDto);

    Boolean deleteAccount(String mobileNumber);
}
