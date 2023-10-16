package com.example.myapplication.tax;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
public class Taxation extends TaxBlock {
    private String m_name;
    private String m_desc;
    private List<Budget> m_taxableAfter;
    private List<TaxPaid> m_taxesPaid;
    public List<Budget> m_input;
    public List<Factor> m_factors;
    private Taxation(String name, String desc) {
        m_name = name;
        m_desc = desc;
        m_taxableAfter = new ArrayList<>();
        m_taxesPaid = new ArrayList<>();
        m_input = new ArrayList<>();
    }
    interface TaxFun {
        void proceed(List<Budget> input, List<Factor> factors, List<TaxPaid> paid, List<Budget> taxableAfter);
    }
    private TaxFun m_applicator;
    public void apply() {
        m_applicator.proceed(m_input, m_factors, m_taxesPaid, m_taxableAfter);
    }
    private static List<Taxation> s_catalogue;
    public static Taxation getTaxation(String name) {
        for (Taxation t: s_catalogue) {
            if (t.m_name.equals(name)) {
                Taxation r = new Taxation(t.m_name, t.m_desc);
                r.m_applicator = t.m_applicator;
                return r;
            }
        }
        return null;
    }
    public void connectInput(List<Budget> input, List<Factor> factors) {
        m_input = input;
        m_factors = factors;
    }
    public List<TaxPaid> getPayments() {
        return m_taxesPaid;
    }
    public List<Budget> getRest() {
        return m_taxableAfter;
    }
    static {
        s_catalogue = new ArrayList<>();
        Taxation t = new Taxation("income", "Income tax ultra simplified");
        t.m_applicator = new TaxFun() {
            public void proceed(List<Budget> input, List<Factor> factors, List<TaxPaid> paid, List<Budget> taxableAfter) {
                double res = 0.0;
                for (Budget i: input) {
                    res += i.getAmount();
                }
                double taxRate = 0.20;
                LocalDate dob = null;
                int taxYear = 0;
                for (Factor f: factors) {
                    if (f.getName().equals("Date of birth")) {
                        dob = (LocalDate) f.getValue();
                    } else if (f.getName().equals("Taxation year")) {
                        taxYear = (int) f.getValue();
                    } else if (f.getName().equals("Spent on professional studies")) {
                        res -= (double) f.getValue();
                    }
                }
                if (ChronoUnit.YEARS.between(dob, LocalDate.of(taxYear, 1, 1)) > 60) {
                    taxRate = 0;
                }
                paid.add(new TaxPaid(res * taxRate, "Finanzamt"));
                taxableAfter.add(new Budget(res * (1 - taxRate)));
            }
        };
        s_catalogue.add(t);
    }
}
