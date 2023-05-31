package com.bevilaqua.aplicativo_unc_m2.app.pages.ui.login;

import com.bevilaqua.aplicativo_unc_m2.data.datasource.model.UserModel;

import java.util.Map;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private final UserModel userModel;

    LoggedInUserView(UserModel user) {
        this.userModel = user;
    }

    String getDisplayName() {
        return userModel.getName();
    }

    String getEmail() {
        return userModel.getEmail();
    }

    String getUid() {
        return userModel.getId();
    }

    Map<String, Object> userToJson() {
        return userModel.toJson();
    }
}