package com.misman.start.service;

import com.misman.start.dto.ChangePassword;
import com.misman.start.dto.Register;
import com.misman.start.model.User;

public interface UserService {

    User register(Register register);

    String resetPassword(Register register);

    void changePassword(ChangePassword changePassword);
}
