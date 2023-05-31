package com.bevilaqua.aplicativo_unc_m2.app.pages.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bevilaqua.aplicativo_unc_m2.R;
import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.LoggedInUser;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.UserModel;
import com.bevilaqua.aplicativo_unc_m2.data.repository.LoginRepository;

import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        // can be launched in a separate asynchronous job
        //Result<LoggedInUser> result = loginRepository.login(username, password);

        Map<String, Object> json = new HashMap<>();
        json.put("email", email);
        json.put("password", password);
        Result<UserModel> result = loginRepository.login(json);

        if (result instanceof Result.Success) {
            UserModel data = ((Result.Success<UserModel>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data)));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void register(String email, String password, String name) {
        // can be launched in a separate asynchronous job
        Map<String, Object> json = new HashMap<>();
        json.put("email", email);
        json.put("password", password);
        json.put("name", name);
        Result<UserModel> result = loginRepository.register(json);


        if (result instanceof Result.Success) {
            UserModel data = ((Result.Success<UserModel>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data)));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void insertCurrenUser() {
        Result<UserModel> result = loginRepository.insertLoggedUser();

        if (result instanceof Result.Success) {
            UserModel data = ((Result.Success<UserModel>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data)));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String email, String password, String name) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, null));
        } else if(!isNameValid(name)){
            loginFormState.setValue(new LoginFormState(null, null, R.string.invalid_name));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isEmailValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isNameValid(String name) {
        return name != null && !name.equals("");
    }
}