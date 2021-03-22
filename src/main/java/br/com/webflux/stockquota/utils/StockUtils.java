package br.com.webflux.stockquota.utils;

import io.netty.util.internal.ObjectUtil;

import java.util.Locale;
import java.util.Objects;

public class StockUtils {
    public static String getFormattedTicket(String ticket) {
        if(Objects.nonNull(ticket) && Locale.getDefault().equals(getLocaleBrazil())) {
            return String.join(".", ticket, "SA");
        }
        return ticket;
    }

    private static Locale getLocaleBrazil() {
        return new Locale("pt", "BR");
    }
}
