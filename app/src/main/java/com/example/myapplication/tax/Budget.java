package com.example.myapplication.tax;

public class Budget extends TaxBlock {
    private double m_amount = 0.0;
    public double getAmount() {
        return m_amount;
    }
    public Budget(double amount) {
        m_amount = amount;
    }
}
