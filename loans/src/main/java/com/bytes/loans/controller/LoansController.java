package com.bytes.loans.controller;

import com.bytes.loans.constants.LoansConstants;
import com.bytes.loans.dto.ErrorResponseDto;
import com.bytes.loans.dto.LoansDto;
import com.bytes.loans.dto.ResponseDto;
import com.bytes.loans.service.LoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Loans in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE loan details"
)
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/loans")
public class LoansController {

    private final LoansService loansService;

    @Operation(
            summary = "Create Loan",
            description = "REST API to create new Loan inside EazyBank"
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
    public ResponseEntity<ResponseDto> createLoan(@RequestParam String mobileNumber){
        log.info( "Creating loan for mobile number: {}", mobileNumber);
        loansService.createLoan(mobileNumber);
        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body(new ResponseDto( LoansConstants.STATUS_201, LoansConstants.MESSAGE_201 ));
    }

    @Operation(
            summary = "Fetch Loan Details",
            description = "REST API to fetch loan details inside EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status 200 SUCCESS"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status 404 NOT FOUND",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber){
        log.info( "Fetching loan details for mobile number: {}", mobileNumber);
        LoansDto loansDto = loansService.fetchLoanDetails(mobileNumber);
        return ResponseEntity
                .status( HttpStatus.OK )
                .body(loansDto);
    }

    @Operation(
            summary = "Update Loan Details",
            description = "REST API to update loan details inside EazyBank"
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
    public ResponseEntity<ResponseDto> updateLoanDetails(@RequestBody LoansDto loansDto){
        log.info( "Updating loan details for mobile number: {}", loansDto.getMobileNumber());
        Boolean isUpdatedLoan =loansService.updateLoanDetails(loansDto);
        if(isUpdatedLoan){
            return ResponseEntity
                    .status( HttpStatus.OK )
                    .body(new ResponseDto( LoansConstants.STATUS_200, LoansConstants.MESSAGE_200 ));

        }else{
            return ResponseEntity
                    .status( HttpStatus.EXPECTATION_FAILED )
                    .body(new ResponseDto( LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE ));
        }
    }


    @Operation(
            summary = "Delete Loan",
            description = "REST API to delete loan inside EazyBank"
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
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLoan(@RequestParam String mobileNumber){
        log.info( "Deleting loan for mobile number: {}", mobileNumber);
        Boolean isDeleted =loansService.deleteLoan(mobileNumber);
        if(!isDeleted){
            return ResponseEntity
                    .status( HttpStatus.EXPECTATION_FAILED )
                    .body(new ResponseDto( LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE ));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
    }


}
