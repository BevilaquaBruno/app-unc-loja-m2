package com.bevilaqua.aplicativo_unc_m2.domain.entity;

import com.bevilaqua.aplicativo_unc_m2.data.sources.local.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductEntity {
    private String id, title, description, created_at, updated_at, user_id;
    private Boolean stocked;
    private Double value;

    public ProductEntity(JSONObject json) throws JSONException {
        this.setId(json.getString("id"));
        this.setTitle(json.getString("title"));
        this.setDescription(json.getString("description"));
        this.setCreated_at(json.getString("created_at"));
        this.setUpdated_at(json.getString("updated_at"));
        this.setStocked(json.getBoolean("stocked"));
        this.setValue(json.getDouble("value"));
        FirebaseAuth auth = ConfigFirebase.getAuth();
        this.setUser_id(auth.getCurrentUser().getUid());
    }

    public void setUser_id(String user_id) { this.user_id = user_id; }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStocked(Boolean stocked) {
        this.stocked = stocked;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUser_id() { return user_id; }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Boolean getStocked() {
        return stocked;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getDescription() {
        return description;
    }

    public Double getValue() {
        return value;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("title", getTitle());
        map.put("description", getDescription());
        map.put("stocked", getStocked());
        map.put("value", getValue());
        map.put("created_at", getCreated_at());
        map.put("updated_at", getUpdated_at());
        map.put("user_id", getUser_id());

        return map;
    }
}
