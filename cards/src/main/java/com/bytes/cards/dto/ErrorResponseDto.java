package com.bytes.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "ErrorResponse holds error details"
)
@Data
@Builder
public class ErrorResponseDto {

    @Schema(description = "API Path which caused the error")
    private String apiPath;
    @Schema(description = "Error Message")
    private String errorMessage;
    @Schema(description = "Error Code")
    private String errorCode;
    @Schema(description = "Time of the Error")
    private LocalDateTime errorTime;
}
