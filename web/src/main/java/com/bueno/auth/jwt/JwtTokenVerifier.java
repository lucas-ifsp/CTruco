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

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;
    private final JwtTokenHelper jwtTokenHelper;

    public JwtTokenVerifier(JwtProperties jwtProperties, JwtTokenHelper jwtTokenHelper) {
        this.jwtProperties = jwtProperties;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(isFromPermittedPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader(jwtProperties.getAuthorizationHeader());
        if (jwtTokenHelper.hasInvalidAuthorization(authorizationHeader)) {
            final String error = "Authorization header is missing or invalid.";
            log.error("Token verification error: {}", error);
            response.addHeader(jwtProperties.getAuthorizationHeader(), error);
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.replace(jwtProperties.getTokenPrefix(), "");
        try {
            final String principal = jwtTokenHelper.extractClaims(token).getSubject();
            final UUID userId = UUID.fromString(principal);
            final var authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("Token verification error: {}", e.getMessage());
            jwtTokenHelper.configureTokenErrorResponse(response, e.getMessage());
        }
    }

    private boolean isFromPermittedPath(HttpServletRequest request) {
        return request.getServletPath().equals("/register")
                || request.getServletPath().equals("/login")
                || request.getServletPath().equals("/refresh-token");
    }
}
