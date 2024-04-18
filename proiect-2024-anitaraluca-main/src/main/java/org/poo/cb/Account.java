package org.poo.cb;

public class Account implements CurrencyAccount{
    private String currency;
    private double balance;

    public Account(String currency) {
        this.currency = currency;
        this.balance = 0.00;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}
