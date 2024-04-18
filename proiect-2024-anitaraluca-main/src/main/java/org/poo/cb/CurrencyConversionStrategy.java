package org.poo.cb;

public interface CurrencyConversionStrategy {
    public double convert(double amount, double exchangeRate,String email);
}
