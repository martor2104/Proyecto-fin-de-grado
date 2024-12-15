package com.api.webReservas.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
    private static final String SECRET_KEY="d3VGNEFvJUNAI2J3Smo1U0JxSDclRzY3U0VeOFhRSndnTEpVa3hYY3Q2TCMydSVLcioyVUE1SDJHY2FeWmtZankqRVJqOXQ3SGZyXkRzamlkV2J2eSRBY3diU2ViUTlmVDRqdyF2elVkNW1WaEZKSiZyZCZoc204WVN2d25WIWI=";

    /**
     * Method responsible for generating the token without claims
     *
     * @param user
     * @return token
     */
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    /**
     * Method responsible for generating the token with claims
     *
     * @param extraClaims
     * @param user
     * @return token
     */
    public String getToken(Map<String,Object> extraClaims, UserDetails user) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Method responsible for getting the key
     *
     * @return clave
     */
    private Key getKey() {
       byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Method responsible for getting the DAS from the token
     *
     * @param token
     * @return DAS
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Method responsible for validating the token
     *
     * @param token
     * @param userDetails
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String das = getUsernameFromToken(token);
        return (das.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Method responsible for getting all the claims
     *
     * @param token
     * @return claims
     */
    private Claims getAllClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Method responsible for getting a claim
     *
     * @param token
     * @param claimsResolver
     * @return claim
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Method responsible for getting the expiration date
     *
     * @param token
     * @return fecha de expiración
     */
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Method responsible for validating if the token has expired
     *
     * @param token
     * @return true si el token ha expirado, false en caso contrario
     */
    public boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
