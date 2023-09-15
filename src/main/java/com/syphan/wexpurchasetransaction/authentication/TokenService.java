package com.syphan.wexpurchasetransaction.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.syphan.wexpurchasetransaction.model.dto.general.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken(UserDto user) {
        return this.generateToken(user.id().toString(), user.name(), user.email());
    }

    public String generateToken(UserEntity user) {
        return this.generateToken(user.getId().toString(), user.getName(), user.getEmail());
    }

    public String generateToken(String id, String name, String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.create()
                    .withIssuer("API - Wex Purchase Transaction")
                    .withSubject(id)
                    .withClaim("name", name)
                    .withClaim("email", email)
                    .withExpiresAt(this.getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("It was not possible to generate the token.");
        }
    }

    public String getSubject(String jwtToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.require(algorithm)
                    .withIssuer("API - Wex Purchase Transaction")
                    .build()
                    .verify(jwtToken)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Invalid token.");
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }
}