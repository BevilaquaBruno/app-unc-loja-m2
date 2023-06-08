package com.bevilaqua.aplicativo_unc_m2.data.datasource;

import android.util.Log;

import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.bevilaqua.aplicativo_unc_m2.domain.entity.ProductEntity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
                .addOnSuccessListener(success -> this.successSave = true)
                .addOnFailureListener(failure -> this.successSave = false);

        return new Result.Success(true);
    }

    public Result<List<ProductEntity>> getProducts(String userId){
        try {
            db = ConfigFirebase.getDb();
            List<ProductEntity> listProducts = null;
            db.collection("products")
                    .whereEqualTo("user_id", userId).get()
                    .addOnSuccessListener(
                            response -> {
                                List<DocumentSnapshot> doc = response.getDocuments();
                                for( int i = 0; i < doc.size(); i++){
                                    DocumentSnapshot currentDoc = doc.get(i);
                                    try {
                                        JSONObject json = new JSONObject();
                                        json.put("user_id", Objects.requireNonNull(currentDoc.get("user_id")).toString());
                                        json.put("title", Objects.requireNonNull(currentDoc.get("title")).toString());
                                        json.put("description", Objects.requireNonNull(currentDoc.get("description")).toString());
                                        json.put("created_at", Objects.requireNonNull(currentDoc.get("created_at")).toString());
                                        json.put("updated_at", Objects.requireNonNull(currentDoc.get("updated_at")).toString());
                                        json.put("stocked", Boolean.parseBoolean(Objects.requireNonNull(currentDoc.get("stocked")).toString()));
                                        json.put("value", Double.parseDouble(Objects.requireNonNull(currentDoc.get("value")).toString()));

                                        listProducts.add(new ProductEntity(json));
                                    } catch (JSONException e) {
                                        Log.i("DataSource Error", e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );
            return new Result.Success<>(listProducts);
        }catch (Exception e){
            return new Result.Error(new IOException("Erro get services", e));
        }
    }
}

