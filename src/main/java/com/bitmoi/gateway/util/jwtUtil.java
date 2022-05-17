package com.bitmoi.gateway.util;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class jwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    public Jwt validateToken(String token) throws Exception {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        // return Jwts.parserBuilder().setSigningKey(key).build().parse(token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return Jwts.parserBuilder().setSigningKey(key).build().parse(token);
        } catch (ExpiredJwtException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        // catch (SignatureException e) {
        // throw new
        // UnauthorizedException(ExceptionMessage.SignatureVerifyToken.getMessage());
        // } catch (IllegalArgumentException e) {
        // throw new
        // UnauthorizedException(ExceptionMessage.IllegalArgumentToken.getMessage());
        // } catch (Exception e) {
        // throw new
        // UnauthorizedException(ExceptionMessage.VerifyFailToken.getMessage());
        // }
    }
}
