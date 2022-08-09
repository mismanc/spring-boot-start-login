package com.misman.start.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

public class SessionUser extends User {

    private final Long id;

    public SessionUser(User user, Long id) {
        super(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), user.getAuthorities());
        this.id = id;
    }

    public SessionUser(long id, String email, String password, boolean active, Set<SimpleGrantedAuthority> authorities) {
        super(email, password, active, true, true, true, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
