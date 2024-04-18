package org.poo.cb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UsdToEuroConversionStrategy implements CurrencyConversionStrategy {
    public double convert(double amount, double exchangeRate,String email) {
        User user = UserDatabase.getUserByEmail(email);
        String currency1 = "USD";
        String currency2 = "EUR";
        Double money3 = 0.00;
        money3 = user.getPortfolio().getAccountByCurrency(currency1).getBalance() - exchangeRate * amount;
        return money3;
    }
}
