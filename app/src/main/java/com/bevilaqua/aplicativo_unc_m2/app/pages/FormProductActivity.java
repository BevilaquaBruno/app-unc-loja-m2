package com.bevilaqua.aplicativo_unc_m2.app.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bevilaqua.aplicativo_unc_m2.R;
import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.ProductDataSource;
import com.bevilaqua.aplicativo_unc_m2.data.repository.ProductRepository;
import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.bevilaqua.aplicativo_unc_m2.domain.entity.ProductEntity;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

public class FormProductActivity extends AppCompatActivity {

    private String action;
    private String id;
    private String title;
    private String description;
    private String created_at;
    private String created_updated;
    private Boolean stocked;
    private Double value;

    TextView txTitle;
    TextView txViewId;
    EditText edtTextTitle;
    EditText edtTextDescription;
    EditText edtTextCreatedAt;
    EditText edtTextUpdatedAt;
    Switch switchStocked;
    EditText edtTextValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_product);

        ActionBar appBar = Objects.requireNonNull(getSupportActionBar());
        Bundle extras = getIntent().getExtras();
        this.action = extras.getString("action");
        if(Objects.equals(this.action, "Create")) {
            appBar.setTitle(R.string.form_app_bar_name_create);
        }else if(Objects.equals(this.action, "Edit")){
            appBar.setTitle(R.string.form_app_bar_name_edit);
        }
        appBar.setDisplayHomeAsUpEnabled(true);

        this.edtTextTitle = findViewById(R.id.edtTextTitle);
        this.edtTextDescription = findViewById(R.id.edtTextDescription);
        this.edtTextCreatedAt = findViewById(R.id.edtTextCreatedAt);
        this.edtTextUpdatedAt = findViewById(R.id.edtTextUpdatedAt);
        this.switchStocked = findViewById(R.id.switchStocked);
        this.edtTextValue = findViewById(R.id.edtTextValue);
        this.txViewId = findViewById(R.id.txViewId);

        this.edtTextTitle.setText(extras.getString("title"));
        this.edtTextDescription.setText(extras.getString("description"));
        this.edtTextCreatedAt.setText(extras.getString("created_at"));
        this.edtTextUpdatedAt.setText(extras.getString("updated_at"));
        this.switchStocked.setChecked(extras.getBoolean("stocked"));
        this.edtTextValue.setText(String.valueOf(extras.getDouble("value")));
        this.txViewId.setText(extras.getString("id"));

        Button btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnSaveProduct.setOnClickListener(v -> {
            this.saveProduct();;
        });
    }

    private void saveProduct() {
        try {
            FirebaseAuth auth = ConfigFirebase.getAuth();
            JSONObject json = new JSONObject();
            json.put("user_id", Objects.requireNonNull(auth.getCurrentUser()).getUid());
            if(Objects.equals(this.action, "Create")) {
                json.put("id", UUID.randomUUID().toString());
            }else if(Objects.equals(this.action, "Edit")) {
                json.put("id", this.txViewId.getText().toString());
            }
            json.put("title", edtTextTitle.getText().toString());
            json.put("description", edtTextDescription.getText().toString());
            json.put("created_at", edtTextCreatedAt.getText().toString());
            json.put("updated_at", edtTextUpdatedAt.getText().toString());
            json.put("stocked", switchStocked.isChecked());
            json.put("value", Double.parseDouble(edtTextValue.getText().toString()));
            ProductEntity product = new ProductEntity(json);

            if(Objects.equals(product.getTitle(), "") || product.getTitle() == null){
                Toast.makeText(this, "Informe o título", Toast.LENGTH_LONG).show();
                return;
            }

            if(Objects.equals(product.getDescription(), "") || product.getDescription() == null){
                Toast.makeText(this, "Informe a descrição", Toast.LENGTH_LONG).show();
                return;
            }

            if(!Objects.equals(product.getUpdated_at(), "") && product.getUpdated_at() != null && product.getStocked()){
                Toast.makeText(this, "Se o produto possui data de saída, ele não pode estar em estoque", Toast.LENGTH_LONG).show();
                return;
            }

            if (!product.getStocked() && (Objects.equals(product.getUpdated_at(), "") || product.getUpdated_at() == null)){
                Toast.makeText(this, "Se o produto não está em estoque precisa ter uma data de saída.", Toast.LENGTH_LONG).show();
                return;
            }

            ProductRepository repository = new ProductRepository(new ProductDataSource());
            Boolean situation = null;
            if(Objects.equals(this.action, "Create")){
                situation = ((Result.Success<Boolean>) repository.saveProduct(product)).getData();
                Log.i("Create", situation.toString());
            }else if(Objects.equals(this.action, "Edit")) {
                situation = ((Result.Success<Boolean>) repository.updateProduct(product)).getData();
                Log.i("Edit", situation.toString());
            }

            assert situation != null;
            if (situation.equals(true)){
                Toast.makeText(this, (this.action.equals("Create") ? "Produto cadastrado com sucesso" : "Produto editado com sucesso"), Toast.LENGTH_LONG).show();
                Intent result = new Intent();
                result.putExtra("product", product.toJson().toString());
                setResult(-1, result);
                finish();
            }else{
                Toast.makeText(this, (this.action.equals("Create") ? "Erro ao cadastrar o serviço" : "Erro ao editar o serviço"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.i("DataSource Error", e.getMessage());
            e.printStackTrace();
        }
        Log.i("Title => ", edtTextTitle.getText().toString());
        Log.i("Description => ", edtTextDescription.getText().toString());
        Log.i("createdAt => ", edtTextCreatedAt.getText().toString());
        Log.i("updatedAt => ", edtTextUpdatedAt.getText().toString());
        Log.i("switchStocked => ", (switchStocked.isChecked())?"true":"false");
        Log.i("value => ", edtTextValue.getText().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == 16908332){
            onBackPressed();
            return (true);
        }
        return(super.onOptionsItemSelected(item));
    }
}