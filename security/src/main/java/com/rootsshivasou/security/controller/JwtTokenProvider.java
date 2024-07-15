package com.rootsshivasou.security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component // création du jwt Token
public class JwtTokenProvider {

    private final String secretKey = "n2ZG8RYGQ9bcZl";


    private long validityInMilliseconds = 3600000; // 1 heure
    

    public String createToken(String username, List<String> roles, String firstName, String lastName, String email, Integer id) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);


        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withClaim("prenom", firstName)
                .withClaim("nom", lastName)
                .withClaim("email", email)
                .withClaim("id", id)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    
}


