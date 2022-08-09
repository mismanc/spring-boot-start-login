package com.misman.start.web;

import com.misman.start.dto.*;
import com.misman.start.security.SessionUser;
import com.misman.start.service.JwtService;
import com.misman.start.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private final JwtService jwtService;

    private final UserService userService;

    public AccountResource(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "Hello World";
    }

    @GetMapping("/with/{name}")
    public String indexWithName(@PathVariable String name) {
        return "Hello " + name + "!";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authenticate(@RequestBody Login login) {
        return ResponseEntity.ok(jwtService.authenticate(login));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid Register register) {
        userService.register(register);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<General> resetPassword(@RequestBody Register register) {
        String encrypted = userService.resetPassword(register);
        // mailService.sendEmail(register.getEmail(), encrypted);
        return ResponseEntity.ok(new General().name("resetPasswordToken").value(encrypted));
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody ChangePassword changePassword) {
        userService.changePassword(changePassword);
        // mailService.sendEmail(register.getEmail(), encrypted);
    }

    @GetMapping("/logged-user")
    public ResponseEntity<String> getLoggedUser() {
        SessionUser sessionUser = (SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(sessionUser.getId() + " : " + sessionUser.getUsername());
    }
}
