package org.poo.cb;

import java.util.ArrayList;
import java.util.List;

public class Portofolio{
    private List<Account> accounts;
    private List<Stock> stocks;
    public Portofolio() {
        this.accounts = new ArrayList<>();
        this.stocks = new ArrayList<>();
    }

    public Portofolio(List<Stock> stocks) {
        this.stocks = new ArrayList<>();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
    public void addStock(Stock stock) {
        stocks.add(stock);
    }
    public Stock getStockByName(String company) {
        for (Stock stock : stocks) {
            if (stock.getCompanyName().equals(company)) {
                return stock;
            }
        }
        return null;
    }

    public Account getAccountByCurrency(String currency) {
        for (Account account : accounts) {
            if (account.getCurrency().equalsIgnoreCase(currency)) {
                return account;
            }
        }
        return null;
    }
    public void listPortofolio() {
        StringBuilder result = new StringBuilder("{\"stocks\":[");
        boolean firstStock = true;
        for (Stock stock : stocks) {
            if (!firstStock) {
                result.append(",");
            }
            String stockName = stock.getCompanyName();
            int amount = stock.getNumberOfStocks();
            result.append("{\"stockname\":\"").append(stockName).append("\",\"amount\":").append(amount).append("}");
            firstStock = false;
        }
        result.append("],\"accounts\":[");
        boolean firstAccount = true;
        for (Account account : accounts) {
            if (!firstAccount) {
                result.append(",");
            }
            String currency = account.getCurrency();
            double balance = account.getBalance();
            result.append("{\"currencyname\":\"").append(currency).append("\",\"amount\":\"").append(String.format("%.2f", balance)).append("\"}");
            firstAccount = false;
        }
        result.append("]}");
        System.out.println(result);
    }



}
