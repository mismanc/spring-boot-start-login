package com.misman.start.service;

import com.misman.start.dto.JWTToken;
import com.misman.start.dto.Login;

public interface JwtService {

    JWTToken authenticate(Login login);

}
