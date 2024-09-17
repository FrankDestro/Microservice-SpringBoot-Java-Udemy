package com.dev.book_service.proxy;

import com.dev.book_service.configuration.FeignClientConfiguration;
import com.dev.book_service.response.Cambio;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cambio-service", configuration = FeignClientConfiguration.class)
public interface CambioProxy {

    @GetMapping(value = "cambio-service/{amount}/{from}/{to}")
    public Cambio getCambio(
            @PathVariable("amount") Double amount,
            @PathVariable("from")String from,
            @PathVariable("to") String to
    );
}
