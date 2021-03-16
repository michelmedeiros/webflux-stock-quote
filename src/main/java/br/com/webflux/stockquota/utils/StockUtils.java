package br.com.webflux.stockquota.utils;

import java.util.Locale;

public class StockUtils {
    public static String getFormattedTicket(String ticket) {
        if(Locale.getDefault().equals(getLocaleBrazil())) {
            return String.join(".", ticket, "SA");
        }
        return ticket;
    }

    private static Locale getLocaleBrazil() {
        return new Locale("pt", "BR");
    }
}
