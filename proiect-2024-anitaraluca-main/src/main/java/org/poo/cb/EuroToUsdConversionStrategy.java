package org.poo.cb;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class EuroToUsdConversionStrategy implements CurrencyConversionStrategy {
    public double convert(double amount, double exchangeRate,String email) {
        User user = UserDatabase.getUserByEmail(email);
        String currency1 = "EUR";
        String currency2 = "USD";
        Double money3 = 0.00;
        money3 = user.getPortfolio().getAccountByCurrency(currency1).getBalance() - exchangeRate * amount;

        return money3;
    }
}

