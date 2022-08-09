package com.misman.start.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.misman.start.dto.ChangePassword;
import com.misman.start.dto.Register;
import com.misman.start.error.BadRequestException;
import com.misman.start.error.ErrorCode;
import com.misman.start.model.User;
import com.misman.start.model.enumeration.Authorities;
import com.misman.start.repository.UserRepository;
import com.misman.start.service.UserService;
import com.misman.start.util.Cryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String VALID_PARAM = "validDate";
    private static final String TOKEN_PARAM = "token";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(Register register) {
        Optional<User> userOptional = userRepository.findByEmail(register.getEmail());
        if (userOptional.isPresent()) {
            throw new BadRequestException(ErrorCode.EMAIL_IN_USE, userOptional.get().getLang());
        }
        User user = new User();
        user.setEmail(register.getEmail());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setAuthorities(Set.of(Authorities.OWNER));
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public String resetPassword(Register register) {
        Optional<User> userOptional = userRepository.findByEmail(register.getEmail());
        if (userOptional.isEmpty()) {
            throw new BadRequestException(ErrorCode.USER_NOT_FOUND).args(register.getEmail());
        }
        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setPasswordRefreshToken(token);
        userRepository.save(user);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TOKEN_PARAM, token);
        jsonObject.addProperty(VALID_PARAM, Instant.now().plus(1, ChronoUnit.DAYS).toString());
        return Cryptor.encrypt(jsonObject.toString(), Base64.getUrlEncoder());
    }

    @Override
    public void changePassword(ChangePassword changePassword) {
        String decrypted = Cryptor.decrypt(changePassword.getResetPasswordToken(), Base64.getUrlDecoder());
        if (decrypted == null) {
            throw new BadRequestException(ErrorCode.NOT_VALID_TOKEN);
        }
        JsonObject jsonObject = JsonParser.parseString(decrypted).getAsJsonObject();
        String validDateStr = jsonObject.get(VALID_PARAM).getAsString();
        Instant validDate = Instant.parse(validDateStr);
        if (validDate.isBefore(Instant.now())) {
            throw new BadRequestException(ErrorCode.NOT_VALID_TOKEN);
        }
        String token = jsonObject.get(TOKEN_PARAM).getAsString();
        Optional<User> userOptional = userRepository.findByPasswordRefreshToken(token);
        if (userOptional.isEmpty()) {
            throw new BadRequestException(ErrorCode.NOT_VALID_TOKEN);
        }
        User user = userOptional.get();
        user.setPasswordRefreshToken(null);
        user.setPassword(passwordEncoder.encode(changePassword.getPassword()));
        userRepository.save(user);
    }
}
