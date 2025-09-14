package com.bytes.accounts.service;

import com.bytes.accounts.dto.CustomerDetailsDto;

public interface CustomerService {

    CustomerDetailsDto getCustomerDetails(String mobileNumber);
}
