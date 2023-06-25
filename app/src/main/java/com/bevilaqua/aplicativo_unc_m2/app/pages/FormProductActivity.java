package com.bevilaqua.aplicativo_unc_m2.app.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

        // adiciona a action bar para poder voltar a tela
        ActionBar appBar = Objects.requireNonNull(getSupportActionBar());
        appBar.setDisplayHomeAsUpEnabled(true);

        // instancia os botoes
        Button btnSaveProduct = findViewById(R.id.btnSaveProduct);
        Button btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        // no click do salvar chama o método para gravar os dados
        btnSaveProduct.setOnClickListener(v -> {
            this.saveProduct();;
        });

        // pega o intent da tela, que são os dados que foram passados para o formulário
        Bundle extras = getIntent().getExtras();
        this.action = extras.getString("action");

        // verifica se a ação passada é de criação ou edição
        if(Objects.equals(this.action, "Create")) {
            // se for uma criação seta a titleBar e o botão de excluir invisível
            appBar.setTitle(R.string.form_app_bar_name_create);
            btnDeleteProduct.setVisibility(View.INVISIBLE);
        }else if(Objects.equals(this.action, "Edit")){
            //se for uma edição seta a titleBar e o botão de excluir visivel
            appBar.setTitle(R.string.form_app_bar_name_edit);
            btnDeleteProduct.setVisibility(View.VISIBLE);

            // seta o action do botão de deletar também
            btnDeleteProduct.setOnClickListener(v -> {
                this.deleteProduct();
            });
        }

        // instancia todos os campos
        this.edtTextTitle = findViewById(R.id.edtTextTitle);
        this.edtTextDescription = findViewById(R.id.edtTextDescription);
        this.edtTextCreatedAt = findViewById(R.id.edtTextCreatedAt);
        this.edtTextUpdatedAt = findViewById(R.id.edtTextUpdatedAt);
        this.switchStocked = findViewById(R.id.switchStocked);
        this.edtTextValue = findViewById(R.id.edtTextValue);
        this.txViewId = findViewById(R.id.txViewId);

        // set o valor de todos os campos
        this.edtTextTitle.setText(extras.getString("title"));
        this.edtTextDescription.setText(extras.getString("description"));
        this.edtTextCreatedAt.setText(extras.getString("created_at"));
        this.edtTextUpdatedAt.setText(extras.getString("updated_at"));
        this.switchStocked.setChecked(extras.getBoolean("stocked"));
        this.edtTextValue.setText(String.valueOf(extras.getDouble("value")));
        this.txViewId.setText(extras.getString("id"));
    }

    private void deleteProduct(){
        //instancia o repository
        ProductRepository productRepository = new ProductRepository(new ProductDataSource());
        //pega o id do txView invisivel
        this.txViewId = findViewById(R.id.txViewId);
        Boolean situation = null;
        //deleta o produto
        situation = ((Result.Success<Boolean>) productRepository.deleteProduct(txViewId.getText().toString())).getData();
        if(situation.equals(true)){
            Toast.makeText(this, "Produto deletado com sucesso!", Toast.LENGTH_LONG).show();
            setResult(-1, null);
            finish();
        }
    }

    private void saveProduct() {
        try {
            // pega o usuário atual para pegar o id dele
            FirebaseAuth auth = ConfigFirebase.getAuth();
            // cria um json do objeto que está sendo savo
            JSONObject json = new JSONObject();
            // coloca o id do usuário
            json.put("user_id", Objects.requireNonNull(auth.getCurrentUser()).getUid());

            // se for uma criação, cria um id random, se não, pega o id do txView
            if(Objects.equals(this.action, "Create")) {
                json.put("id", UUID.randomUUID().toString());
            }else if(Objects.equals(this.action, "Edit")) {
                json.put("id", this.txViewId.getText().toString());
            }

            // adiciona os demais dados
            json.put("title", edtTextTitle.getText().toString());
            json.put("description", edtTextDescription.getText().toString());
            json.put("created_at", edtTextCreatedAt.getText().toString());
            json.put("updated_at", edtTextUpdatedAt.getText().toString());
            json.put("stocked", switchStocked.isChecked());
            json.put("value", Double.parseDouble(edtTextValue.getText().toString()));
            ProductEntity product = new ProductEntity(json);

            // validações
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

            // instancia o repository
            ProductRepository repository = new ProductRepository(new ProductDataSource());
            Boolean situation = null;
            // chama o método de acordo com a ação que for passada
            if(Objects.equals(this.action, "Create")){
                // cria o produto
                situation = ((Result.Success<Boolean>) repository.saveProduct(product)).getData();
                Log.i("Create", situation.toString());
            }else if(Objects.equals(this.action, "Edit")) {
                // edita o produto
                situation = ((Result.Success<Boolean>) repository.updateProduct(product)).getData();
                Log.i("Edit", situation.toString());
            }

            assert situation != null;
            if (situation.equals(true)){
                // mostra a mensagem de acordo com a ação passada e retorna os dados com sucesso
                Toast.makeText(this, (this.action.equals("Create") ? "Produto cadastrado com sucesso" : "Produto editado com sucesso"), Toast.LENGTH_LONG).show();
                Intent result = new Intent();
                result.putExtra("product", product.toJson().toString());
                setResult(-1, result);
                finish();
            }else{
                //exibe algum erro na falha ao cadastrar
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