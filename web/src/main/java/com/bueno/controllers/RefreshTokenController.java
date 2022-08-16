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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

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
    public void getNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authorizationHeader = request.getHeader(jwtProperties.getAuthorizationHeader());

        if (jwtTokenHelper.hasInvalidAuthorizationHeader(authorizationHeader)) {
            final String error = "Authorization header is missing or invalid.";
            log.error("Refresh token verification error: {}", error);
            response.addHeader(jwtProperties.getAuthorizationHeader(), error);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        final String refresh_token = authorizationHeader.replace(jwtProperties.getTokenPrefix(), "");
        try {
            final var principal = jwtTokenHelper.extractClaims(refresh_token).getSubject();
            final var userId = UUID.fromString(principal);

            final var user = (ApplicationUser) applicationUserService.loadUserById(userId);
            final var issuer = request.getRequestURL().toString();
            final var token = jwtTokenHelper.createAccessToken(user, issuer);

            response.addHeader(jwtProperties.getAuthorizationHeader(), jwtProperties.getTokenPrefix() + token);
            log.info("Refreshed access token for: {}", user.getUsername());

        } catch (Exception e) {
            log.error("Refresh token verification error: {}", e.getMessage());
            jwtTokenHelper.configureTokenErrorResponse(response, e.getMessage());
        }
    }
}
