package br.com.webflux.stockquota.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quote {
    public static final MathContext MATH_CONTEXT = new MathContext(2);
    private String ticket;
    private BigDecimal price;
    private Instant instant;

    public Quote(String ticket, BigDecimal price) {
        this.ticket = ticket;
        this.price = price;
    }

    public Quote(String ticket, Double price) {
        this.ticket = ticket;
        this.price = new BigDecimal(price, MATH_CONTEXT);
    }
}
