package com.bevilaqua.aplicativo_unc_m2.app.pages;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bevilaqua.aplicativo_unc_m2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

public class ListProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        FloatingActionButton btnAddProduct = findViewById(R.id.btnAddProduct);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFormProduct("Create");
            }
        });
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                    }
                }
            });

    public void openFormProduct(String action) {
        Intent intent = new Intent(this, FormProductActivity.class);
        intent.putExtra("action", action);
        intent.putExtra("id", "");
        intent.putExtra("title", "Título");
        intent.putExtra("description", "Descrição");
        intent.putExtra("created_at", "");
        intent.putExtra("updated_at", "");
        intent.putExtra("stocked", false);
        intent.putExtra("value", 0.00);

        someActivityResultLauncher.launch(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}