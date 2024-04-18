package org.poo.cb;

public class AccountFactory {
    public static CurrencyAccount createAccount(String currency) {
        return new Account(currency);
    }
}
