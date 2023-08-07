package com.hamza.cafe.security;

import com.hamza.cafe.entities.Role;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(String username, Role role);

    boolean isTokenValid(String token, UserDetails userDetails);
    Claims extractAllClaims(String token);
}
