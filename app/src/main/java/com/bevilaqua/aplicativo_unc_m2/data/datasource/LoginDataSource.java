package com.bevilaqua.aplicativo_unc_m2.data.datasource;

import android.util.Log;

import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.LoggedInUser;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.UserModel;
import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth auth;

    public Result<UserModel> login(Map<String, Object> json) {

        try {
            String email = (String) json.get("email");
            String password = (String) json.get("password");

            auth = ConfigFirebase.getAuth();
            assert email != null && password != null;

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(task -> {
                        Log.d("SIGNSUCCESS", "signInWithEmail:success");
                    }).addOnFailureListener(task -> {
                        Log.w("SIGNFAILURE", "signInWithEmail:failure", task.getCause());
                    });
            UserModel userModel =
                    new UserModel(Objects.requireNonNull(auth.getCurrentUser().getUid()),
                            auth.getCurrentUser().getDisplayName(),
                            auth.getCurrentUser().getEmail()
                    );
            return new Result.Success<>(userModel);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<UserModel> createUser(Map<String, Object> json) {

        try {
            String email = (String) json.get("email");
            String password = (String) json.get("password");
            String name = (String) json.get("name");

            auth = ConfigFirebase.getAuth();
            assert email != null && password != null && name != null;

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(task -> {
                        Log.d("SIGNUPSUCCESS", "createUserWithEmail:success");
                        Objects.requireNonNull(Objects.requireNonNull(task.getUser()).updateProfile(
                                new UserProfileChangeRequest.Builder().setDisplayName(
                                        name
                                ).build()
                        ));
                    }).addOnFailureListener(task -> {
                        Log.w("SIGNUPFAILURE", "createUserWithEmail:failure", task.getCause());
                    });
            return new Result.Success<>(new UserModel("", email, name));
        } catch (Exception e) {
            Log.e("Error => ", (e.getMessage() != null? e.getMessage() : "Error"));
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<UserModel> insertCurrentUser() {

        try {
            FirebaseFirestore db = ConfigFirebase.getDb();
            FirebaseAuth firebaseAuth = ConfigFirebase.getAuth();
            UserModel userModel = new UserModel(firebaseAuth.getCurrentUser().getUid(),
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