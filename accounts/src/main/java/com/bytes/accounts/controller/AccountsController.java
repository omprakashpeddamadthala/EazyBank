package com.bytes.accounts.controller;

import com.bytes.accounts.constants.AccountsConstants;
import com.bytes.accounts.dto.AccountsContactInfoDto;
import com.bytes.accounts.dto.CustomerDto;
import com.bytes.accounts.dto.ErrorResponseDto;
import com.bytes.accounts.dto.ResponseDto;
import com.bytes.accounts.service.AccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Accounts in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE account details"
)
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Validated
public class AccountsController {

    private final AccountsService accountsService;

    @Value("${build.version}")
    private String buildVersion;

    private final Environment environment;

    private final  AccountsContactInfoDto accountsContactInfoDto;

    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer &  Account inside EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status 201 CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status 500 INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountsService.createAccount( customerDto );
        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( new ResponseDto( AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201 ) );
    }


    @Operation(
            summary = "Fetch Account REST API",
            description = "REST API to fetch Customer &  Account details from EazyBank by mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status 500 INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam @Pattern(regexp = "^[6-9]\\d{9}$", message = "please provide a valid  mobile number")
                                                           String mobileNumber) {
        CustomerDto customerDto = accountsService.fetchAccountDetails( mobileNumber );
        return ResponseEntity
                .status( HttpStatus.OK )
                .body( customerDto );
    }


    @Operation(
            summary = "Update Account Details REST API",
            description = "REST API to update Customer &  Account details based on a account number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        Boolean isUpdated = accountsService.updateAccountDetails( customerDto );
        if (isUpdated) {
            return ResponseEntity
                    .status( HttpStatus.OK )
                    .body( new ResponseDto( AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200 ) );
        } else {
            return ResponseEntity
                    .status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( new ResponseDto( AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500 ) );
        }
    }


    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer &  Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestParam @Pattern(regexp = "^[6-9]\\d{9}$", message = "please provide a valid  mobile number")
                                           String mobileNumber) {
        boolean isDeleted = accountsService.deleteAccount( mobileNumber );
        if (isDeleted) {
            return ResponseEntity
                    .status( HttpStatus.OK )
                    .body( new ResponseDto( AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200 ) );
        } else {
            return ResponseEntity
                    .status( HttpStatus.EXPECTATION_FAILED )
                    .body( new ResponseDto( AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE ) );
        }
    }

    @Operation(
            summary = "Build Version REST API",
            description = "REST API to fetch the build version of the application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Java Version REST API",
            description = "REST API to fetch the Java version of the application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }


    @Operation(
            summary = "Contact Info REST API",
            description = "REST API to fetch the Contact Info of the application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    })
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }
}
