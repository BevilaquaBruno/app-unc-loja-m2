package com.bevilaqua.aplicativo_unc_m2.app.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bevilaqua.aplicativo_unc_m2.R;

public class ListProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}