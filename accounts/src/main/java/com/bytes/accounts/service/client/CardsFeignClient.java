package com.bytes.accounts.service.client;

import com.bytes.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards")
public interface CardsFeignClient {

     @GetMapping("/v1/api/fetch")
     ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber,@RequestHeader("eazybank-correlation-id") String correlationId) ;
}
