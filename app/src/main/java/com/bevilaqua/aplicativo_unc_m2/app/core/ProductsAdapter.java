package com.bevilaqua.aplicativo_unc_m2.app.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bevilaqua.aplicativo_unc_m2.R;
import com.bevilaqua.aplicativo_unc_m2.app.pages.FormProductActivity;
import com.bevilaqua.aplicativo_unc_m2.app.pages.ListProductsActivity;
import com.bevilaqua.aplicativo_unc_m2.domain.entity.ProductEntity;

import java.util.ArrayList;

public class ProductsAdapter extends ArrayAdapter<ProductEntity> {

    private final ArrayList<ProductEntity> listProducts;
    private final Context context;

    public ProductsAdapter(@NonNull Context context, @NonNull ArrayList<ProductEntity> list) {
        super(context,0, list);

        this.listProducts = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View view, ViewGroup group){
        View view1 = null;
        if( listProducts != null ){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // montagem da tela
            view1 = inflater.inflate(R.layout.list_products, group, false);

            //buscar elementos
            TextView title = view1.findViewById(R.id.txViewTitle);
            TextView description = view1.findViewById(R.id.txViewDescription);
            TextView stocked = view1.findViewById(R.id.txViewStocked);
            TextView updatedAt = view1.findViewById(R.id.txViewUpdatedAt);
            Button btnEditProduct = view1.findViewById(R.id.btnEditProduct);

            //setar os valores
            ProductEntity productEntity = listProducts.get(position);

            title.setText(productEntity.getTitle());
            description.setText(productEntity.getDescription());
            stocked.setText((productEntity.getStocked())?"Em estoque R$ "
                    + productEntity.getValue().toString(): "Fora de estoque");
            updatedAt.setText((productEntity.getUpdated_at() == null)?"-":productEntity.getUpdated_at());
            btnEditProduct.setOnClickListener(i -> {
                if(this.context instanceof ListProductsActivity){
                    ((ListProductsActivity) this.context).openFormProduct("Edit", productEntity);
                }
            });
        }

        return view1;
    }
}
