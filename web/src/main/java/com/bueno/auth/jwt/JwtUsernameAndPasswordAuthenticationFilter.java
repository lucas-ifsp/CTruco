/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.auth.jwt;

import com.bueno.auth.security.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final int MILLIS_OF_MINUTES = 60000;
    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      SecretKey secretKey,
                                                      JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.secretKey = secretKey;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            final var authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthenticationRequest.class);

            final var authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            log.error("Authentication error: " + e.getMessage());
            response.addHeader(jwtProperties.getAuthorizationHeader(), "Invalid username or password");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {

        var user = (ApplicationUser) authResult.getPrincipal();
        final String token = Jwts.builder()
                .setSubject(user.getUuid().toString())
                .claim("userId", user.getUuid())
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .setIssuer(request.getRequestURL().toString())
                .setExpiration(new Date(currentTimeMillis() + jwtProperties.getTokenExpirationAfterMinutes() * MILLIS_OF_MINUTES))
                .signWith(secretKey)
                .compact();

        final String refreshToken = Jwts.builder()
                .setSubject(user.getUuid().toString())
                .setIssuedAt(new Date())
                .setIssuer(request.getRequestURL().toString())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtProperties.getRefreshTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();

        response.addHeader(jwtProperties.getAuthorizationHeader(), jwtProperties.getTokenPrefix() + token);
        response.addHeader(jwtProperties.getRefreshTokenHeader(), jwtProperties.getTokenPrefix() + refreshToken);
        log.info("Granted access and refresh tokens for: {}", user.getUsername());
    }
}
