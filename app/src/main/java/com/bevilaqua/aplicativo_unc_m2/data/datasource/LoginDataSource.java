package com.bevilaqua.aplicativo_unc_m2.data.datasource;

import android.util.Log;

import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.UserModel;
import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth auth;

    public Task<AuthResult> login(Map<String, Object> json) {

        try {
            String email = (String) json.get("email");
            String password = (String) json.get("password");

            auth = ConfigFirebase.getAuth();
            assert email != null && password != null;

            return auth.signInWithEmailAndPassword(email, password);
        } catch (Exception e) {
            return null;
        }
    }

    public Task<AuthResult> createUser(Map<String, Object> json) {

        try {
            String email = (String) json.get("email");
            String password = (String) json.get("password");
            String name = (String) json.get("name");

            auth = ConfigFirebase.getAuth();
            assert email != null && password != null && name != null;

            return auth.createUserWithEmailAndPassword(email, password);
        } catch (Exception e) {
            Log.e("Error => ", (e.getMessage() != null? e.getMessage() : "Error"));
            return null;
        }
    }

    public Result<UserModel> insertCurrentUser() {

        try {
            FirebaseFirestore db = ConfigFirebase.getDb();
            FirebaseAuth firebaseAuth = ConfigFirebase.getAuth();
            UserModel userModel = new UserModel(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(),
                    firebaseAuth.getCurrentUser().getEmail(), firebaseAuth.getCurrentUser().getDisplayName());
            db.collection("users").add(userModel.toJson());

            return new Result.Success<>(userModel);
        }catch(Exception e){
            return new Result.Error(new IOException("Error inserting", e));
        }
    }

    public void logout() {
        try {
            auth = ConfigFirebase.getAuth();
            auth.signOut();
        }catch (Exception e){
            Log.e("error => ", e.getMessage());
        }
    }
}