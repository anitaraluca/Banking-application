package org.poo.cb;

public interface CurrencyAccount {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
}
