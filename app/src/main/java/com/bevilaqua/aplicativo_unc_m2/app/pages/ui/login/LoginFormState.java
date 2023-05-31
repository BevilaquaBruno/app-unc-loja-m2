package com.bevilaqua.aplicativo_unc_m2.app.pages.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer nameError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer nameError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.nameError = nameError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.emailError = null;
        this.passwordError = null;
        this.nameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}