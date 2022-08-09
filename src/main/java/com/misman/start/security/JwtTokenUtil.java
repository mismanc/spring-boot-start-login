package com.misman.start.security;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    private static final long serialVersionUID = -2550185165626007488L;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String ID_KEY = "userId";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token-validity}")
    private long validity;

    private long tokenValidityInMilliseconds;

    private Key key;

    private final Gson gson;

    private final Type listTypeAuths;

    public JwtTokenUtil() {
        this.gson = new Gson();
        this.listTypeAuths = new TypeToken<List<SimpleGrantedAuthority>>() {
        }.getType();
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        if (!StringUtils.hasText(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " + "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = 1000 * validity;
    }

    public String createToken(Authentication authentication) {
        String authorities = gson.toJson(authentication.getAuthorities());
        Date validity = new Date(System.currentTimeMillis() + tokenValidityInMilliseconds);
        SessionUser sessionUser = (SessionUser) authentication.getPrincipal();
        return Jwts.builder().setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(ID_KEY, sessionUser.getId())
                .signWith(key, SignatureAlgorithm.HS512).setExpiration(validity).compact();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = getAllClaimsFromToken(jwt);
        long userId = claims.get(ID_KEY, Long.class);
        List<GrantedAuthority> authorities = gson.fromJson(claims.get(AUTHORITIES_KEY).toString(), listTypeAuths);
        User principal = new User(claims.getSubject(), "", authorities);
        SessionUser sessionUser = new SessionUser(principal, userId);
        return new UsernamePasswordAuthenticationToken(sessionUser, jwt, authorities);
    }
}
