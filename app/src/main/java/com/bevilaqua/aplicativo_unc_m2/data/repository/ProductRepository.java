package com.bevilaqua.aplicativo_unc_m2.data.repository;

import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.ProductDataSource;
import com.bevilaqua.aplicativo_unc_m2.domain.entity.ProductEntity;

import java.util.List;

public class ProductRepository {
    private static volatile ProductRepository instance;

    private final ProductDataSource dataSource;

    private List<ProductEntity> listProducts;

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

    public Result<Boolean> updateProduct(ProductEntity product){
        return dataSource.updateProduct(product);
    }

    private List<ProductEntity> setListProducts(List<ProductEntity> list) {
        listProducts.addAll(list);
        return listProducts;
    }

    public Result<List<ProductEntity>> getProducts(String userId ){
        Result<List<ProductEntity>> list = dataSource.getProducts(userId);
        if( list instanceof Result.Success){
            setListProducts(((Result.Success<List<ProductEntity>>) list).getData());
        }
        return list;
    }
}
