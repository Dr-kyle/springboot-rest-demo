package com.kyle.demo.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kyle.demo.exception.OauthException;

/**
 * @author kz37
 */
public class JwtUtil {
    private JwtUtil() {}

    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static String createToken(String email, String userName, String role, String shortName, String secret, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create().withIssuer(issuer).withSubject(email)
                .withClaim("username", userName)
                .withClaim("role", role)
                .withClaim("shortname", shortName)
                .sign(algorithm);
        return token;
    }

    public static DecodedJWT parseToken(String token, String secret, String issuer) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
            return verifier.verify(token);
        } catch (Exception e) {
            throw new OauthException("token is invalid");
        }

    }

    public static String getEmail(String token, String secret, String issuer) {
        DecodedJWT jwt = parseToken(token, secret, issuer);
        return jwt.getSubject();
    }

    public static String getUsername(String token, String secret, String issuer) {
        DecodedJWT jwt = parseToken(token, secret, issuer);
        return jwt.getClaim("username").asString();
    }

    public static String getRole(String token, String secret, String issuer) {
        DecodedJWT jwt = parseToken(token, secret, issuer);
        return jwt.getClaim("role").asString();
    }

    public static String getShortName(String token, String secret, String issuer) {
        DecodedJWT jwt = parseToken(token, secret, issuer);
        return jwt.getClaim("shortname").asString();
    }

    public static void main(String[] args) {
        System.out.println(createToken("email", "kyle", "admin", "kz37", "kyle", "haha"));
        System.out.println(createToken("email", "kyle", "admin", "kz37", "kyle", "haha"));

    }
}
