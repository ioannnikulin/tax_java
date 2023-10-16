package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.example.myapplication.tax.*;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnAddGerman).setId(View.generateViewId());
    }

    public void addGerman(View view) {
        TaxationChain tc = TaxationChain.getChain("Test chain");
        ViewParent p = view.getParent();
        if (!(p instanceof ConstraintLayout) || tc == null) return;
        tc.draw((ConstraintLayout) p);
    }
}