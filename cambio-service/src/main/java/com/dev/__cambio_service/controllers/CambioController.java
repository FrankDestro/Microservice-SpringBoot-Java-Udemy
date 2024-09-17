package com.dev.__cambio_service.controllers;

import com.dev.__cambio_service.models.entities.Cambio;
import com.dev.__cambio_service.repositories.CambioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("cambio-service")
@Tag(name = "cambio endpoint")
public class CambioController {

    private final Environment environment;

    private final CambioRepository repository;

    @Operation(summary = "Get cambio from currency", method = "GET")
    @GetMapping(value = "/{amount}/{from}/{to}")
    public Cambio getCambio(
            @PathVariable("amount")BigDecimal amount,
            @PathVariable("from")String from,
            @PathVariable("to") String to
            ){
        log.info("Received request cambio service");
        var cambio = repository.findByFromAndTo(from, to);
        if(cambio == null) throw new RuntimeException("Currency unsupported");

        var port = environment.getProperty("local.server.port");
        BigDecimal conversionFactor = cambio.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount);
        cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
        cambio.setEnvironment(port);
        return cambio;
    }
}
