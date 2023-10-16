package com.example.myapplication.tax;

import android.view.View;

abstract class TaxBlock {
    private View uil;
    public void connect(View view) {
        uil = view;
    }
}