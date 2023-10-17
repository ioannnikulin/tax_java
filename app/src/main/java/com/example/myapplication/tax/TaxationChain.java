package com.example.myapplication.tax;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.*;
public class TaxationChain extends TaxBlock {
    private List<Factor> m_factors;
    private String m_name;
    private Budget m_netto; // for result AND for static methods
    public TaxationChain addFactor(Factor f) {
        m_factors.add(f);
        return this;
    }
    private Budget m_inputFactory;
    //public Budget createBudget()
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
    private TextView addField(ConstraintLayout layout, ConstraintSet cs, String name, String defValue, List<View> ets) {
        TextView tv = new TextView(layout.getContext());
        tv.setId(View.generateViewId());
        tv.setText(name);
        layout.addView(tv);
        EditText et = new EditText(layout.getContext());
        et.setText(defValue);
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
        if (ets.size() > 0) {
            cs.connect(et.getId(), ConstraintSet.TOP, ets.get(ets.size() - 1).getId(), ConstraintSet.BOTTOM, 20);
            cs.connect(et.getId(), ConstraintSet.START, ets.get(0).getId(), ConstraintSet.START);
        } else { // never, unless someone removes header TextView
            cs.connect(et.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 20);
        }
        ets.add(et);
        return tv;
    }
    private ConstraintLayout drawFactors(CardView card) {
        ConstraintLayout layout = new ConstraintLayout(card.getContext());
        layout.setId(View.generateViewId());
        card.addView(layout);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);

        TextView h = new TextView(card.getContext());
        h.setId(View.generateViewId());
        h.setText("Factors:");
        cs.constrainHeight(h.getId(), ConstraintSet.WRAP_CONTENT);
        layout.addView(h);

        cs.connect(h.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        cs.connect(h.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START);

        List<View> ets = new ArrayList<>();
        ets.add(h);
        int rightmost_et_idx = -1, rightmost_tv_len = 0;
        TextView rightmost_tv = null;
        for (int i = 0; i < m_factors.size(); i ++) {
            if (m_factors.get(i).getMandatory()) {
                if (m_factors.get(i).getName().length() > rightmost_tv_len) {
                    rightmost_tv_len = m_factors.get(i).getName().length();
                    rightmost_et_idx = i;
                    rightmost_tv = addField(layout, cs, m_factors.get(i).getName(), "default value", ets);
                } else {
                    addField(layout, cs, m_factors.get(i).getName(), "default value", ets);
                }
                m_factors.get(i).connect(ets.get(ets.size() - 1));
            }
        }
        if (rightmost_et_idx != -1) {
            cs.connect(ets.get(rightmost_et_idx).getId(), ConstraintSet.START, rightmost_tv.getId(), ConstraintSet.END, 10);
            if (rightmost_et_idx != 0) {
                cs.connect(ets.get(0).getId(), ConstraintSet.START, ets.get(rightmost_et_idx).getId(), ConstraintSet.START);
            }
        }
        int[] etIds = new int[ets.size()];
        for (int i = 0; i < ets.size(); i ++) etIds[i] = ets.get(i).getId();
        Barrier br = new Barrier(card.getContext());
        br.setId(View.generateViewId());
        br.setType(Barrier.END);
        br.setReferencedIds(etIds);
        layout.addView(br);
        // TODO: check whether it will update itself on adding new fields

        Button btn_addFactor = new Button(card.getContext());
        btn_addFactor.setText("+");
        btn_addFactor.setId(View.generateViewId());
        cs.constrainHeight(btn_addFactor.getId(), ConstraintSet.WRAP_CONTENT);
        cs.constrainWidth(btn_addFactor.getId(), ConstraintSet.WRAP_CONTENT);
        btn_addFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "DUMMY: adding optional factor", Toast.LENGTH_LONG).show();
            }
        });
        layout.addView(btn_addFactor);
        cs.connect(btn_addFactor.getId(), ConstraintSet.START, br.getId(), ConstraintSet.END, 20);
        cs.connect(btn_addFactor.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        cs.connect(btn_addFactor.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);

        cs.applyTo(layout);
        return layout;
    }
    private ConstraintLayout drawInputs(CardView card) {
        ConstraintLayout layout = new ConstraintLayout(card.getContext());
        layout.setId(View.generateViewId());
        card.addView(layout);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);

        Barrier br = new Barrier(card.getContext());
        br.setId(View.generateViewId());
        br.setType(Barrier.RIGHT);
        layout.addView(br);
        // TODO: check whether it will update itself on adding new fields

        Button btn_addFactor = new Button(card.getContext());
        btn_addFactor.setText("*");
        btn_addFactor.setId(View.generateViewId());
        cs.constrainHeight(btn_addFactor.getId(), ConstraintSet.WRAP_CONTENT);
        cs.constrainWidth(btn_addFactor.getId(), ConstraintSet.WRAP_CONTENT);
        btn_addFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "DUMMY: adding optional factor", Toast.LENGTH_LONG).show();
            }
        });
        layout.addView(btn_addFactor);

        //cs.connect(btn_addFactor.getId(), ConstraintSet.START, ets.get(0).getId(), ConstraintSet.END, 20);
        cs.connect(btn_addFactor.getId(), ConstraintSet.START, br.getId(), ConstraintSet.END, 20);
        cs.connect(btn_addFactor.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        cs.connect(btn_addFactor.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        cs.applyTo(layout);
        return layout;
    }
    private ConstraintLayout drawNetto(CardView card) {//TODO: dummy
        ConstraintLayout l = new ConstraintLayout(card.getContext());
        l.setId(View.generateViewId());
        card.addView(l);
        return l;
    }
    private ConstraintLayout drawTaxation(CardView card) {//TODO: dummy
        ConstraintLayout l = new ConstraintLayout(card.getContext());
        l.setId(View.generateViewId());
        card.addView(l);
        return l;
    }
    public int draw(ConstraintLayout parent) {
        CardView card = new CardView(parent.getContext());
        card.setId(View.generateViewId());
        parent.addView(card);
        card.setContentPadding(20, 20, 20, 20);

        ConstraintLayout factor_l = drawFactors(card);
        //ConstraintLayout input_l = drawInputs(card);
        /*ConstraintLayout taxation_l = drawTaxation(card);
        View output = drawNetto(card);


        cs.connect(output.getId(), ConstraintSet.TOP, taxation_l.getId(), ConstraintSet.BOTTOM, 40);
        cs.connect(output.getId(), ConstraintSet.START, taxation_l.getId(), ConstraintSet.START);
        cs.connect(taxation_l.getId(), ConstraintSet.TOP, input_l.getId(), ConstraintSet.BOTTOM, 40);
        cs.connect(taxation_l.getId(), ConstraintSet.START, input_l.getId(), ConstraintSet.START);
        cs.connect(input_l.getId(), ConstraintSet.TOP, factor_l.getId(), ConstraintSet.BOTTOM, 40);
        cs.connect(input_l.getId(), ConstraintSet.START, factor_l.getId(), ConstraintSet.START);


*/ConstraintSet cs = new ConstraintSet();
        cs.clone(parent);
        //cs.connect(input_l.getId(), ConstraintSet.TOP, card.getId(), ConstraintSet.TOP);
        cs.connect(factor_l.getId(), ConstraintSet.START, card.getId(), ConstraintSet.START);
        //cs.connect(input_l.getId(), ConstraintSet.BOTTOM, input_l.getId(), ConstraintSet.TOP);
        //cs.connect(input_l.getId(), ConstraintSet.START, card.getId(), ConstraintSet.START);
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
    //private Budget m_netto;
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
