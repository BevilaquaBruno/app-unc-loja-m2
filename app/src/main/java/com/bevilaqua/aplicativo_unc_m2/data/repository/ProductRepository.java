package com.bevilaqua.aplicativo_unc_m2.data.repository;

import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.ProductDataSource;
import com.bevilaqua.aplicativo_unc_m2.domain.entity.ProductEntity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.List;

public class ProductRepository {
    private static volatile ProductRepository instance;

    private final ProductDataSource dataSource;

    private List<ProductEntity> listProducts = Collections.emptyList();

    public ProductRepository(ProductDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ProductRepository getInstance(ProductDataSource dataSource) {
        if (instance == null) {
            instance = new ProductRepository(dataSource);
        }
        return instance;
    }

    public Result<Boolean> saveProduct(ProductEntity product){
        return dataSource.saveProduct(product);
    }

    public Result<Boolean> deleteProduct(String productId){
        return dataSource.deleteProduct(productId);
    }

    public Result<Boolean> updateProduct(ProductEntity product){
        return dataSource.updateProduct(product);
    }

    private List<ProductEntity> setListProducts(List<ProductEntity> list) {
        this.listProducts.addAll(list);
        return this.listProducts;
    }

    public Task<QuerySnapshot> getProducts(String userId ){
        return dataSource.getProducts(userId);
    }
}
