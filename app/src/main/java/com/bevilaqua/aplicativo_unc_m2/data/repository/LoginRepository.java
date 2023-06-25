package com.bevilaqua.aplicativo_unc_m2.data.repository;

import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.LoginDataSource;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Map;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private final LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private UserModel user = null;

    // private constructor : singleton access
    public LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setUserModel(UserModel user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Task<AuthResult> login(Map<String, Object> json) {
        // handle login
        return dataSource.login(json);
    }

    public Task<AuthResult> register(Map<String, Object> json) {
        // handle login
        return dataSource.createUser(json);
    }

    public Result<UserModel> insertLoggedUser() {
        // handle login
        Result<UserModel> result = dataSource.insertCurrentUser();
        if (result instanceof Result.Success) {
            setUserModel(((Result.Success<UserModel>) result).getData());
        }
        return result;
    }
}