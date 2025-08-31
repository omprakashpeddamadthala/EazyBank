package com.bytes.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Error response details"
)
@Data
@Builder
public class ErrorResponseDto {

    @Schema(description = "API path where the error occurred")
    private String apiPath;

    @Schema(description = "HTTP status code of the error")
    private String errorCode;

    @Schema(description = "Detailed error message")
    private String errorMessage;

    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime errorTime;
}
