package org.poo.cb;

public class Exchange {
    private CurrencyConversionStrategy conversionStrategy;

    public void setConversionStrategy(CurrencyConversionStrategy strategy) {
        this.conversionStrategy = strategy;
    }

    public double convertCurrency(double amount, double exchangeRate,String email) {
        if (conversionStrategy != null) {
            return conversionStrategy. convert(amount, exchangeRate, email);
        } else {
            throw new IllegalStateException("No conversion strategy set.");
        }
    }
}
