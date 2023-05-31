package com.bevilaqua.aplicativo_unc_m2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bevilaqua.aplicativo_unc_m2.app.pages.ListProductsActivity;
import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = ConfigFirebase.getAuth();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null){
            setContentView(R.layout.activity_main);
        }else{
            startActivity(new Intent(this, ListProductsActivity.class));
        }
    }
}