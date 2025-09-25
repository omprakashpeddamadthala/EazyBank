package com.bytes.accounts.controller;

import com.bytes.accounts.dto.CustomerDetailsDto;
import com.bytes.accounts.dto.ErrorResponseDto;
import com.bytes.accounts.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer REST APIs for Customer Details in EazyBank", description = "REST API to fetch Customer Details from EazyBank")
@RestController
@RequestMapping("/v1/api/customer")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CustomerController {

    private final CustomerService customerService;


    @Operation(summary = "Fetch Customer REST API",
                description = "REST API to fetch Customer  details from EazyBank by mobile number")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                    @ApiResponse(responseCode = "500", description = "HTTP Status 500 INTERNAL SERVER ERROR",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    @RequestMapping("/fetch")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestHeader("eazybank-correlation-id") String correlationId,
            @RequestParam @Pattern(regexp = "^[6-9]\\d{9}$", message = "please provide a valid  mobile number")
                                                                       String mobileNumber) {
        log.debug( "eazy-bank-correlation-id found :{}",correlationId );
        CustomerDetailsDto customerDetailsDto = customerService.getCustomerDetails( mobileNumber,correlationId );
        return ResponseEntity
                .status( HttpStatus.OK )
                .body( customerDetailsDto );
    }
}
