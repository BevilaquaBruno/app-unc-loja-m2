package com.bevilaqua.aplicativo_unc_m2.data.datasource;

import android.util.Log;

import com.bevilaqua.aplicativo_unc_m2.data.Result;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.LoggedInUser;
import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.UserModel;
import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

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

            auth = ConfigFirebase.getAuth();
            assert email != null && password != null;

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(task -> {
                        Log.d("SIGNUPSUCCESS", "createUserWithEmail:success");
                        /*Objects.requireNonNull(Objects.requireNonNull(task.getUser()).updateProfile(
                                new UserProfileChangeRequest.Builder().setDisplayName(
                                        name
                                ).build()
                        ));*/
                    }).addOnFailureListener(task -> {
                        Log.w("SIGNUPFAILURE", "createUserWithEmail:failure", task.getCause());
                    });
            UserModel userModel =
                    new UserModel(Objects.requireNonNull(auth.getCurrentUser().getUid()),
                            auth.getCurrentUser().getDisplayName(),
                            auth.getCurrentUser().getEmail()
                    );

            FirebaseFirestore db = ConfigFirebase.getDb();
            db.collection("users").add(userModel.toJson());
            return new Result.Success<>(userModel);
        } catch (Exception e) {
            Log.e("Error => ", e.getMessage());
            return new Result.Error(new IOException("Error logging in", e));
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