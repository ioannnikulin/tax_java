package com.example.myapplication.tax;

public class TaxPaid extends TaxBlock {

    public TaxPaid(double amount, String collector) {
        m_amount = amount;
        m_collector = collector;
    }
    private double m_amount = 0.0;
    private String m_collector = "Finanzamt";
}
