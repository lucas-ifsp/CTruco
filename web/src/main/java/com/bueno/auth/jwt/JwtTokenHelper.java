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
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtTokenHelper {

    public static final int MILLIS_OF_MINUTES = 60000;
    public static final int SECONDS_OF_DAY = 86400;
    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;

    public JwtTokenHelper(SecretKey secretKey, JwtProperties jwtProperties) {
        this.secretKey = secretKey;
        this.jwtProperties = jwtProperties;
    }

    public String createAccessToken(ApplicationUser user, String issuer) {
        return Jwts.builder()
                .setSubject(user.getUuid().toString())
                .claim("userId", user.getUuid())
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setExpiration(getAccessTokenExpiration())
                .signWith(secretKey)
                .compact();
    }

    private Date getAccessTokenExpiration() {
        return new Date(currentTimeMillis() + jwtProperties.getTokenExpirationAfterMinutes() * MILLIS_OF_MINUTES);
    }

    public Cookie createRefreshTokenCookie(ApplicationUser user, String issuer) {
        final String token = Jwts.builder()
                .setSubject(user.getUuid().toString())
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setExpiration(getRefreshTokenExpiration())
                .signWith(secretKey)
                .compact();
        Cookie cookie = new Cookie(jwtProperties.getRefreshTokenProperty(), token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(getCookieExpiration());
        cookie.setPath("/");
        return cookie;
    }

    private java.sql.Date getRefreshTokenExpiration() {
        return java.sql.Date.valueOf(LocalDate.now().plusDays(jwtProperties.getRefreshTokenExpirationAfterDays()));
    }

    private int getCookieExpiration() {
        return SECONDS_OF_DAY * jwtProperties.getRefreshTokenExpirationAfterDays();
    }

    public void configureTokenErrorResponse(HttpServletResponse response, String message) throws IOException {
        final String headerError = jwtProperties.getTokenPrefix() + " error=" + message;
        response.addHeader(jwtProperties.getAuthorizationHeader(), headerError);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, String> error = new HashMap<>();
        error.put("ErrorDescription", message);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    public boolean hasInvalidAuthorization(String authorizationHeader) {
        return Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ");
    }

    public Claims extractClaims(String token) {
        final Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        return claimsJws.getBody();
    }
}
