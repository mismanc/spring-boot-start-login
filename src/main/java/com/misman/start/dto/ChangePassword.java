package com.misman.start.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChangePassword {

    @NotNull
    @NotEmpty
    private String resetPasswordToken;
    private String password;

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
