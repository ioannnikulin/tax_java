package com.example.myapplication.tax;

import java.util.ArrayList;
import java.util.List;

public class TestBudget extends Budget {
    private TestBudget(String source, double amount) {
        super(source, amount);
    }
    static {
        m_catalogue.put("Netto", "The money the taxpayer gets on their hands after all legal operations");
        m_catalogue.put("Brutto salary", "Money got from direct employment");
        m_catalogue.put("Forestry", "Forestry-related income");
    }
    public static List<String> getConstructibleBudgets() {
        List<String> res = new ArrayList<>();
        for (String k: m_catalogue.keySet()) {
            if (m_scope.getOrDefault(k, -1) == 0) res.add(k);
        }
        return res;
    }
    public static Budget getBudget(String name, double amount) {
        if (m_catalogue.containsKey(name)) return new TestBudget(name, amount);
        return null;
    }
}
