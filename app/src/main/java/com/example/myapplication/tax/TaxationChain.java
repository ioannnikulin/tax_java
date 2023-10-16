package com.example.myapplication.tax;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.*;
public class TaxationChain extends TaxBlock {
    private List<Factor> m_factors;
    private String m_name;
    public TaxationChain addFactor(Factor f) {
        m_factors.add(f);
        return this;
    }
    private TaxationChain() {
        m_factors = new ArrayList<>();
    }
    private TaxationChain(TaxationChain t) {
        m_name = t.m_name;
        m_applicator = t.m_applicator;
        m_factors = new ArrayList<>();
        for (Factor f: t.m_factors) {
            addFactor(Objects.requireNonNull(Factor.getFactor(f.getName())).setMandatory(f.getMandatory()));
        }
    }

    private int addField(ConstraintLayout layout, String name, String defValue, List<View> ets) {
        return 0;
    }
    private ConstraintLayout drawFactors(CardView card) {
        card.setContentPadding(20, 20, 20, 20);

        ConstraintLayout layout = new ConstraintLayout(card.getContext());
        layout.setId(View.generateViewId());
        card.addView(layout);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);

        List<View> ets = new ArrayList<>();
        int righmost_et_idx = -1;
        TextView rightmost_tv = null;
        for (Factor f: m_factors) {
            if (f.getMandatory()) {
                TextView tv = new TextView(layout.getContext());
                tv.setId(View.generateViewId());
                tv.setText(f.getName());
                layout.addView(tv);
                EditText et = new EditText(layout.getContext());
                et.setId(View.generateViewId());
                et.setEms(10);
                layout.addView(et);
                tv.setMaxEms(5);
                cs.constrainHeight(tv.getId(), ConstraintSet.WRAP_CONTENT);
                cs.constrainHeight(et.getId(), ConstraintSet.WRAP_CONTENT);
                cs.connect(tv.getId(), ConstraintSet.BASELINE,
                        et.getId(), ConstraintSet.BASELINE);
                cs.connect(layout.getId(), ConstraintSet.START,
                        tv.getId(), ConstraintSet.START, 10);
                if (righmost_et_idx > -1) {
                    cs.connect(et.getId(), ConstraintSet.TOP, ets.get(ets.size() - 1).getId(), ConstraintSet.BOTTOM, 20);
                    if (tv.getText().length() > rightmost_tv.getText().length()) {
                        rightmost_tv = tv;
                        righmost_et_idx = ets.size();
                    }
                } else {
                    rightmost_tv = tv;
                    righmost_et_idx = 0;
                    cs.connect(et.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 20);
                }
                ets.add(et);
            }
        }
        if (righmost_et_idx == -1) {
            System.out.println("No mandatory factors");
            return layout;
        }
        cs.connect(ets.get(righmost_et_idx).getId(), ConstraintSet.START, rightmost_tv.getId(), ConstraintSet.END, 10);
        for (View et: ets) {
            if (et != ets.get(righmost_et_idx))
                cs.connect(et.getId(), ConstraintSet.START, ets.get(righmost_et_idx).getId(), ConstraintSet.START);
        }
        cs.applyTo(layout);
        return layout;
    }
    private ConstraintLayout drawInputs(CardView card) { //TODO: dummy
        ConstraintLayout l = new ConstraintLayout(card.getContext());
        l.setId(View.generateViewId());
        card.addView(l);
        return l;
    }
    private ConstraintLayout drawNetto(CardView card) {//TODO: dummy
        return drawInputs(card);
    }
    private ConstraintLayout drawTaxation(CardView card) {//TODO: dummy
        return drawInputs(card);
    }
    /*private ConstraintLayout drawInputs(CardView card) {
        ConstraintLayout layout = new ConstraintLayout(card.getContext());
        layout.setId(View.generateViewId());
        card.addView(layout);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(10, 10, 10, 10);
        layout.setLayoutParams(lp);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);

        List<View> ets = new ArrayList<>();
        int righmost_et_idx = -1;
        TextView rightmost_tv = null;
        for (Factor f: m_factors) {
            if (f.getMandatory()) {
                TextView tv = new TextView(layout.getContext());
                tv.setId(View.generateViewId());
                tv.setText(f.getName());
                layout.addView(tv);
                EditText et = new EditText(layout.getContext());
                et.setId(View.generateViewId());
                et.setEms(10);
                layout.addView(et);
                tv.setMaxEms(5);
                cs.constrainHeight(tv.getId(), ConstraintSet.WRAP_CONTENT);
                cs.constrainHeight(et.getId(), ConstraintSet.WRAP_CONTENT);
                cs.connect(tv.getId(), ConstraintSet.BASELINE,
                        et.getId(), ConstraintSet.BASELINE);
                cs.connect(layout.getId(), ConstraintSet.START,
                        tv.getId(), ConstraintSet.START, 10);
                if (righmost_et_idx > -1) {
                    cs.connect(et.getId(), ConstraintSet.TOP, ets.get(ets.size() - 1).getId(), ConstraintSet.BOTTOM, 20);
                    if (tv.getText().length() > rightmost_tv.getText().length()) {
                        rightmost_tv = tv;
                        righmost_et_idx = ets.size();
                    }
                } else {
                    rightmost_tv = tv;
                    righmost_et_idx = 0;
                    cs.connect(et.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 20);
                }
                ets.add(et);
            }
        }
        if (righmost_et_idx == -1) {
            System.out.println("No mandatory factors");
            return layout;
        }
        cs.connect(ets.get(righmost_et_idx).getId(), ConstraintSet.START, rightmost_tv.getId(), ConstraintSet.END, 10);
        for (View et: ets) {
            if (et != ets.get(rightmost_et_idx))
                cs.connect(et.getId(), ConstraintSet.START, ets.get(righmost_et_idx).getId(), ConstraintSet.START);
        }
        cs.applyTo(layout);
        return layout;
    }*/
    public int draw(ConstraintLayout parent) {
        CardView card = new CardView(parent.getContext());
        card.setId(View.generateViewId());
        parent.addView(card);

        ConstraintLayout factor_l = drawFactors(card);
        /*ConstraintLayout input_l = drawInputs(card);
        ConstraintLayout taxation_l = drawTaxation(card);
        View output = drawNetto(card);


        cs.connect(output.getId(), ConstraintSet.TOP, taxation_l.getId(), ConstraintSet.BOTTOM, 40);
        cs.connect(output.getId(), ConstraintSet.START, taxation_l.getId(), ConstraintSet.START);
        cs.connect(taxation_l.getId(), ConstraintSet.TOP, input_l.getId(), ConstraintSet.BOTTOM, 40);
        cs.connect(taxation_l.getId(), ConstraintSet.START, input_l.getId(), ConstraintSet.START);
        cs.connect(input_l.getId(), ConstraintSet.TOP, factor_l.getId(), ConstraintSet.BOTTOM, 40);
        cs.connect(input_l.getId(), ConstraintSet.START, factor_l.getId(), ConstraintSet.START);


*/ConstraintSet cs = new ConstraintSet();
        cs.clone(parent);
        cs.connect(factor_l.getId(), ConstraintSet.TOP, card.getId(), ConstraintSet.TOP);
        cs.connect(factor_l.getId(), ConstraintSet.START, card.getId(), ConstraintSet.START);
        cs.applyTo(parent);
        return 0; // TODO: id of entry point later
    }
    interface TaxChFun {
        void proceed(List<Budget> input, List<Factor> factors, Taxation entry, List<TaxPaid> paid, Budget netto);
    }
    private TaxChFun m_applicator;
    public void apply() {
        m_applicator.proceed(m_input, m_factors, m_entryPoint, m_paid, m_netto);
    }
    private Taxation m_entryPoint;
    private Budget m_netto;
    private List<TaxPaid> m_paid;
    private List<Budget> m_input;
    private static List<TaxationChain> s_catalogue;
    public static TaxationChain getChain(String name) {
        for (TaxationChain t: s_catalogue) {
            if (t.m_name.equals(name)) {
                return new TaxationChain(t);
            }
        }
        return null;
    }
    static {
        s_catalogue = new ArrayList<>();
        TaxationChain tc = new TaxationChain()
                .addFactor(Objects.requireNonNull(Factor.getFactor("Date of birth")).setMandatory(true))
                .addFactor(Objects.requireNonNull(Factor.getFactor("Taxation year")).setMandatory(true))
                .addFactor(Objects.requireNonNull(Factor.getFactor("Spent on professional studies")))
                ;
        tc.m_name = "Test chain";
        tc.m_entryPoint = Taxation.getTaxation("income");
        tc.m_applicator = new TaxChFun() {
            public void proceed(List<Budget> input, List<Factor> factors, Taxation entry, List<TaxPaid> paid, Budget netto) {
                entry.connectInput(input, factors);
                entry.apply();
                paid = entry.getPayments();
                netto = entry.getRest().get(0);
            }
        };
        s_catalogue.add(tc);
    }
}
