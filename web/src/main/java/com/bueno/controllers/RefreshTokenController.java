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

package com.bueno.controllers;


import com.bueno.auth.jwt.JwtProperties;
import com.bueno.auth.security.ApplicationUser;
import com.bueno.auth.security.ApplicationUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/refresh-token")
public class RefreshTokenController {

    public static final int MILLIS_OF_MINUTES = 60000;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;

    public RefreshTokenController(ApplicationUserService applicationUserService, SecretKey secretKey, JwtProperties jwtProperties) {
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtProperties = jwtProperties;
    }

    @GetMapping
    public void getNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authorizationHeader = request.getHeader(jwtProperties.getAuthorizationHeader());

        if (hasInvalidAuthorizationHeader(authorizationHeader)) {
            final String error = "Authorization header is missing or invalid.";
            log.error("Refresh token verification error: {}", error);
            response.addHeader(jwtProperties.getAuthorizationHeader(), error);
            return;
        }

        final String refresh_token = authorizationHeader.replace(jwtProperties.getTokenPrefix(), "");
        try {
            final Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refresh_token);

            final Claims body = claimsJws.getBody();
            final String principal = body.getSubject();
            final UUID userId = UUID.fromString(principal);

            final var user = (ApplicationUser) applicationUserService.loadUserById(userId);

            final String token = Jwts.builder()
                    .setSubject(user.getUuid().toString())
                    .claim("userId", user.getUuid())
                    .claim("username", user.getUsername())
                    .setIssuedAt(new Date())
                    .setIssuer(request.getRequestURL().toString())
                    .setExpiration(new Date(currentTimeMillis() + jwtProperties.getTokenExpirationAfterMinutes() * MILLIS_OF_MINUTES))
                    .signWith(secretKey)
                    .compact();

            response.addHeader(jwtProperties.getAuthorizationHeader(), jwtProperties.getTokenPrefix() + token);
            log.info("Refreshed access token for: {}", user.getUsername());

        } catch (Exception e) {
            log.error("Refresh token verification error: {}", e.getMessage());
            final String headerError = jwtProperties.getTokenPrefix() + " error=" + e.getMessage();
            response.addHeader(jwtProperties.getAuthorizationHeader(), headerError);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, String> error = new HashMap<>();
            error.put("ErrorDescription", e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }

    private boolean hasInvalidAuthorizationHeader(String authorizationHeader) {
        return Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ");
    }
}
