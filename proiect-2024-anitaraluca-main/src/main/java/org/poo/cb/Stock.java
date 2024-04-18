package org.poo.cb;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Stock {
    private String companyName;
    private List<Double> values;
    private int numberOfStocks;

    public int getNumberOfStocks() {
        return numberOfStocks;
    }

    public void setNumberOfStocks(int numberOfStocks) {
        this.numberOfStocks = numberOfStocks;
    }

    public Stock(String companyName, List<Double> values) {
        this.companyName = companyName;
        this.values = new ArrayList<>(values); // Inițializare lista de valori cu valorile primite ca parametru
    }

    public Stock(String companyName) {
        this.companyName = companyName;
        this.values = new ArrayList<>(); // Inițializare lista de valori
    }

    public String getCompanyName() {
        return companyName;
    }

    public void addValue(double value) {
        values.add(value);
    }
    public void printStock() {
        System.out.print("Company Name: " + companyName);
        System.out.print("Stock Values:");
        for (Double value : values) {
            System.out.print(value);
        }
    }
}

