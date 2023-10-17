package com.example.myapplication.tax;

import java.util.HashMap;
import java.util.Map;

abstract public class Budget extends TaxBlock {
    protected static Map<String, String> m_catalogue = new HashMap<>();
    protected static Map<String, Integer> m_scope = new HashMap<>();
    protected static final Integer in = 0; // users can create
    protected static final Integer mid = 1; // users cannot create or view
    protected static final Integer out = 2; // users can view, not create
    public static String description(String of) {
        return m_catalogue.getOrDefault(of, "UNKNOWN SOURCE");
    }
    private double m_amount = 0.0;
    private String m_source;
    public double getAmount() {
        return m_amount;
    }
    protected Budget(String source, double amount) {
        m_source = source;
        m_amount = amount;
    }
}
