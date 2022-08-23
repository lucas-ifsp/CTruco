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
import com.bueno.auth.jwt.JwtTokenHelper;
import com.bueno.auth.security.ApplicationUser;
import com.bueno.auth.security.ApplicationUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/refresh-token")
public class RefreshTokenController {

    private final ApplicationUserService applicationUserService;
    private final JwtProperties jwtProperties;
    private final JwtTokenHelper jwtTokenHelper;

    public RefreshTokenController(ApplicationUserService applicationUserService, JwtProperties jwtProperties,
                                  JwtTokenHelper jwtTokenHelper) {
        this.applicationUserService = applicationUserService;
        this.jwtProperties = jwtProperties;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @GetMapping
    public void getNewAccessToken(
            @CookieValue(value = "refresh-token", defaultValue = "") String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        if (Strings.isNullOrEmpty(refreshToken)) {
            final String error = "Refresh token is missing or invalid.";
            log.error("Refresh token verification error: {}", error);
            response.addHeader(jwtProperties.getAuthorizationHeader(), error);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        final String jwtToken = refreshToken.replace(jwtProperties.getTokenPrefix(), "");
        try {
            final var principal = jwtTokenHelper.extractClaims(jwtToken).getSubject();
            final var userId = UUID.fromString(principal);

            final var user = (ApplicationUser) applicationUserService.loadUserById(userId);
            final var issuer = request.getRequestURL().toString();
            final var token = jwtTokenHelper.createAccessToken(user, issuer);

            response.addHeader(jwtProperties.getAuthorizationHeader(), jwtProperties.getTokenPrefix() + token);

            final Map<String, String> body = new HashMap<>();
            body.put("uuid", user.getUuid().toString());
            body.put("username", user.getUsername());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), body);

            log.info("Refreshed access token for: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Refresh token verification error: {}", e.getMessage());
            jwtTokenHelper.configureTokenErrorResponse(response, e.getMessage());
        }
    }

    @DeleteMapping
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authorizationHeader = request.getHeader(jwtProperties.getAuthorizationHeader());
        if (jwtTokenHelper.hasInvalidAuthorization(authorizationHeader)) {
            final String error = "Authorization header is missing or invalid.";
            log.error("Token verification error: {}", error);
            response.addHeader(jwtProperties.getAuthorizationHeader(), error);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        final String jwt = authorizationHeader.replace(jwtProperties.getTokenPrefix(), "");
        try {
            final var principal = jwtTokenHelper.extractClaims(jwt).getSubject();
            final var userId = UUID.fromString(principal);
            final var user = (ApplicationUser) applicationUserService.loadUserById(userId);

            Cookie expiredToken = new Cookie(jwtProperties.getRefreshTokenProperty(), "");
            expiredToken.setHttpOnly(true);
            expiredToken.setMaxAge(0);
            expiredToken.setPath("/");

            response.addCookie(expiredToken);
            response.setStatus(HttpStatus.NO_CONTENT.value());

            log.info("User {} has been logged out.", user.getUsername());
        } catch (Exception e) {
            log.error("Token verification error: {}", e.getMessage());
            jwtTokenHelper.configureTokenErrorResponse(response, e.getMessage());
        }
    }
}
