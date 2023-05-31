package com.bevilaqua.aplicativo_unc_m2.data.sources.local;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfigFirebase {
    private static FirebaseFirestore db;
    private static FirebaseAuth auth;

    public static FirebaseFirestore getDb(){
        if(db == null){
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

    public static FirebaseAuth getAuth(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
