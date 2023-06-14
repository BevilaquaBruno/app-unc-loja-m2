package com.bevilaqua.aplicativo_unc_m2.app.pages;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bevilaqua.aplicativo_unc_m2.R;
import com.bevilaqua.aplicativo_unc_m2.app.core.ProductsAdapter;
import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.ProductDataSource;
import com.bevilaqua.aplicativo_unc_m2.data.repository.ProductRepository;
import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.bevilaqua.aplicativo_unc_m2.domain.entity.ProductEntity;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListProductsActivity extends AppCompatActivity {

    private ArrayList<ProductEntity> listProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        FloatingActionButton btnAddProduct = findViewById(R.id.btnAddProduct);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("id", "");
                    json.put("title", "");
                    json.put("description", "");
                    json.put("created_at", "");
                    json.put("updated_at", "");
                    json.put("user_id", "");
                    json.put("stocked", false);
                    json.put("value", 0.00);
                    openFormProduct("Create", new ProductEntity(json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getProducts();
    }

    void getProducts() {
        Log.i("Getting Products", "LOL");
        listProducts = new ArrayList<>();

        ProductRepository productRepository = ProductRepository.getInstance(new ProductDataSource());
        FirebaseAuth auth = ConfigFirebase.getAuth();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        Task<QuerySnapshot> snapshot = productRepository.getProducts(user.getUid().toString());
        snapshot.addOnSuccessListener(result -> {
            List<DocumentSnapshot> doc = result.getDocuments();
            List<ProductEntity> list = new ArrayList<>();
            for ( int i = 0; i < doc.size(); i++ ) {
                try {
                    DocumentSnapshot document = doc.get(i);
                    JSONObject json = new JSONObject();
                    json.put("id", document.getReference().getId());
                    json.put("title", document.getString("title"));
                    json.put("description", document.getString("description"));
                    json.put("created_at", document.getString("created_at"));
                    json.put("updated_at", document.getString("updated_at"));
                    json.put("user_id", document.getString("user_id"));
                    json.put("stocked", document.getBoolean("stocked"));
                    json.put("value", document.getDouble("value"));
                    list.add(new ProductEntity(json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            setList(list);
            createListView();
        });
    }

    private void createListView(){
        ListView listView = findViewById(R.id.lv_products);
        ArrayAdapter<ProductEntity> adapter = new ProductsAdapter(this, listProducts);
        listView.setAdapter(adapter);
    }

    private void setList(List<ProductEntity> list) {
        this.listProducts = (ArrayList<ProductEntity>) list;
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

                        getProducts();
                    }
                }
            });


    public void openFormProduct(String action, ProductEntity product) {
        Intent intent = new Intent(this, FormProductActivity.class);
        intent.putExtra("action", action);
        intent.putExtra("id", product.getId());
        intent.putExtra("title", product.getTitle());
        intent.putExtra("description", product.getDescription());
        intent.putExtra("created_at", product.getCreated_at());
        intent.putExtra("updated_at", product.getUpdated_at());
        intent.putExtra("stocked", product.getStocked());
        intent.putExtra("value", product.getValue());

        someActivityResultLauncher.launch(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}