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
import com.bevilaqua.aplicativo_unc_m2.app.pages.ui.login.LoginActivity;
import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.LoginDataSource;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.ProductDataSource;
import com.bevilaqua.aplicativo_unc_m2.data.repository.LoginRepository;
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
    public ListView listView;
    public ArrayAdapter<ProductEntity> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        // instancia os dois botões que tem na tela
        FloatingActionButton btnAddProduct = findViewById(R.id.btnAddProduct);
        FloatingActionButton btnLogout = findViewById(R.id.btnLogout);

        // seta o click do botão de logout
        btnLogout.setOnClickListener(v -> {
            LoginRepository loginRepository = new LoginRepository(new LoginDataSource());
            loginRepository.logout();
            startActivity(new Intent(this, LoginActivity.class));
        });

        // seta o click do botão de adicionar produto
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cria um json vazio para ser os dados do produto
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
                    // abre o form com os dados vazios
                    openFormProduct("Create", new ProductEntity(json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getProducts("Create");
    }

    void getProducts(String action) {
        Log.i("Getting Products", "LOL");
        // cria a lista e instancia o repository
        listProducts = new ArrayList<>();
        ProductRepository productRepository = ProductRepository.getInstance(new ProductDataSource());

        // pega o usuário logado para filtrar o itens da lita
        FirebaseAuth auth = ConfigFirebase.getAuth();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;

        // cria a snapshot para lista de produtos, eu sei que isso deveria ser feito no datasource
        // mas né...
        Task<QuerySnapshot> snapshot = productRepository.getProducts(user.getUid().toString());
        snapshot.addOnSuccessListener(result -> {
            // pega a lista de documentos
            List<DocumentSnapshot> doc = result.getDocuments();
            List<ProductEntity> list = new ArrayList<>();
            // itera os dados da lista
            for ( int i = 0; i < doc.size(); i++ ) {
                try {
                    //para cada produto cria um documento
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
                    // adiciona ele na lista
                    list.add(new ProductEntity(json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // seta a lista com os produtos que foram buscados
            setList(list);

            // verifica se o que foi feito foi uma alteração ou uma criação de um produto
            if(Objects.equals(action, "Update")){
                // se for uma alteração, limpa o adapter e adiciona novamente os novos dados
                this.adapter.clear();
                this.adapter.addAll(this.listProducts);
                this.adapter.notifyDataSetChanged();
            }else{
                // se for uma criação então cria o listview
                createListView();
            }
        });
    }

    private void createListView(){
        // cria o list view e seta o adapter
        this.listView = findViewById(R.id.lv_products);
        this.adapter = new ProductsAdapter(this, listProducts);
        this.listView.setAdapter(adapter);
    }

    private void setList(List<ProductEntity> list) {
        this.listProducts = (ArrayList<ProductEntity>) list;
    }

    public void updateAdapter(){
        getProducts("Update");
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // atualiza a lista de produtos com o adapter
                        updateAdapter();
                    }
                }
            });


    public void openFormProduct(String action, ProductEntity product) {
        // abre o form do produto com os dados passados
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