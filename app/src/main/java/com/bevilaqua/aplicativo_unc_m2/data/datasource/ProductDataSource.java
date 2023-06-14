package com.bevilaqua.aplicativo_unc_m2.data.datasource;

import android.util.Log;

import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.bevilaqua.aplicativo_unc_m2.domain.entity.ProductEntity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ProductDataSource {
    FirebaseFirestore db;

    private boolean successSave;
    public Result<Boolean> saveProduct(ProductEntity product) {
        db = ConfigFirebase.getDb();

        db.collection("products").add(product.toJson())
                .addOnSuccessListener(success -> this.successSave = true)
                .addOnFailureListener(failure -> this.successSave = false);

        return new Result.Success(true);
    }

    public Result<Boolean> updateProduct(ProductEntity product) {
        db = ConfigFirebase.getDb();

        db.collection("products").document(product.getId()).update(product.toJson())
                .addOnSuccessListener(success -> {
                    Log.i("Sucesso => ", "Sucesso");
                    this.successSave = true;})
                .addOnFailureListener(failure -> {
                    Log.i("Failure => ", failure.getMessage());
                    this.successSave = false;
                });

        return new Result.Success(true);
    }

    public Task<QuerySnapshot> getProducts(String userId){
        try {
            Log.i("USERID =>", userId);
            db = ConfigFirebase.getDb();

            return db.collection("products").whereEqualTo("user_id", userId)
                    .get();
        }catch (Exception e){
            Log.i("Error", e.getMessage());
            //return new Result.Error(new IOException("Erro get services", e));
            return null;
        }
    }
}
