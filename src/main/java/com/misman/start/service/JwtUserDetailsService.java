package com.misman.start.service;

import com.misman.start.model.User;
import com.misman.start.repository.UserRepository;
import com.misman.start.security.SessionUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("No user found with email: " + username);
        }
        User user = userOptional.get();

        return new SessionUser(user.getId(), user.getEmail(), user.getPassword(), user.isActive(), user.getAuthorities().stream().map(r -> new SimpleGrantedAuthority(r.name())).collect(Collectors.toSet()));
    }
}
