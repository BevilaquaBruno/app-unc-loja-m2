package com.bevilaqua.aplicativo_unc_m2.app.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bevilaqua.aplicativo_unc_m2.R;

import java.util.Objects;

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

        this.edtTextTitle.setText(extras.getString("title"));
        this.edtTextDescription.setText(extras.getString("description"));
        this.edtTextCreatedAt.setText(extras.getString("created_at"));
        this.edtTextUpdatedAt.setText(extras.getString("updated_at"));
        this.switchStocked.setChecked(extras.getBoolean("stocked"));
        this.edtTextValue.setText(extras.getString("updated_at"));

        Button btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnSaveProduct.setOnClickListener(v -> {
            this.saveProduct();;
        });
    }

    private void saveProduct() {
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