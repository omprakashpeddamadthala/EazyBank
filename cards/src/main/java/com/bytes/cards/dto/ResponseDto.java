package com.bytes.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(
        name = "Response",
        description = "Response holds response details"
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

    @Schema(description =" Status Code of the response")
    private String statusCode;
    @Schema(description = "Status Message of the response")
    private String statusMessage;
}
