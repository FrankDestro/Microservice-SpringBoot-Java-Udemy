package com.dev.book_service.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Cambio {

    private Long id;
    private String from;
    private String to;
    private Double conversionFactor;
    private BigDecimal ConvertedValue;
    private String environment;
}
