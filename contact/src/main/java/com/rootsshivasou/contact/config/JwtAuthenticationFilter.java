package com.rootsshivasou.contact.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
 private final String secretKey = "n2ZG8RYGQ9bcZl";

 @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    // Ajout d'un log pour indiquer que le filtre est exécuté
    System.out.println("JwtAuthenticationFilter: Checking for JWT token");

    String jwt = getJwtFromRequest(request);

    if (jwt != null) {
        System.out.println("JwtAuthenticationFilter: JWT token found: " + jwt);

        if (validateToken(jwt)) {
            System.out.println("JwtAuthenticationFilter: JWT token is valid");

            String username = getUsernameFromJWT(jwt);
            List<SimpleGrantedAuthority> authorities = getAuthorities(jwt);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Stocker l'ID de l'utilisateur dans la session de la requête
            Integer userId = extractUserIdFromJWT(jwt);
            request.getSession().setAttribute("userId", userId);
        } else {
            System.out.println("JwtAuthenticationFilter: JWT token is invalid");
        }
    } else {
        System.out.println("JwtAuthenticationFilter: No JWT token found");
    }

    filterChain.doFilter(request, response);
}


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String jwt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("JwtAuthenticationFilter: Token validation error - " + e.getMessage());
            return false;
        }
    }

    private String getUsernameFromJWT(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getSubject();
    }

    private List<SimpleGrantedAuthority> getAuthorities(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);

        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        return java.util.Arrays.stream(roles)
                                .map(SimpleGrantedAuthority::new) 
                               .collect(Collectors.toList());
    }

    private Integer extractUserIdFromJWT(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getClaim("id").asInt();
    }
    
}
