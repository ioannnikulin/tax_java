package com.example.myapplication.tax;
import java.time.*;
import java.util.*;

class Factor extends TaxBlock {
    private String m_name;
    private String m_desc;
    private Object m_value;
    private boolean m_mandatory = false;
    private Factor(String name, String desc, Object value) {
        m_name = name;
        m_desc = desc;
        m_value = value;
    }
    public Factor setValue(Object value) {
        m_value = value;
        return this;
    }
    public Factor setMandatory(boolean m) {
        m_mandatory = m;
        return this;
    }
    public boolean getMandatory() {
        return m_mandatory;
    }
    public String getName() {
        return m_name;
    }
    public Object getValue() {
        return m_value;
    }
    public static Factor getFactor(String name) {
        for (Factor f: s_catalogue) {
            if (f.m_name == name) {
                return new Factor(f.m_name, f.m_desc, f.m_value);
            }
        }
        return null;
    }
    private static List<Factor> s_catalogue;
    static {
        s_catalogue = new ArrayList<>();
        // TODO: will have to create subclasses for overriding toString for default values, for format checks
        s_catalogue.add(new Factor("Date of birth", "YYYY-MM-DD", LocalDate.now()));
        s_catalogue.add(new Factor("Taxation year", "YYYY", LocalDate.now().getYear()));
        s_catalogue.add(new Factor("Spent on professional studies", "euros", 0.0));
    }
};