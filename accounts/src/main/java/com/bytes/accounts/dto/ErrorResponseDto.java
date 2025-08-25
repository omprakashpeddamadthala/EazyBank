package com.bytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Schema to hold Error Response information"
)
@Data
@Builder
public class ErrorResponseDto {

    @Schema(
            description = "API path which caused the error"
    )
    private String apiPath;

    @Schema(
            description = "Error message describing the error"
    )
    private String errorMessage;

    @Schema(
            description = "Error code associated with the error"
    )
    private String errorCode;

    @Schema(
            description = "Timestamp when the error occurred"
    )
    private LocalDateTime errorTime;
}
