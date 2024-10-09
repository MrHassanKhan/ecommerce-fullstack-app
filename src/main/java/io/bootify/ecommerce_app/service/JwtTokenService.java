package io.bootify.ecommerce_app.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import io.bootify.ecommerce_app.model.JwtUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JwtTokenService {

    private static final Duration JWT_TOKEN_VALIDITY = Duration.ofMinutes(30);

    private final Algorithm rsa256;
    private final JWTVerifier verifier;

    public JwtTokenService(@Value("classpath:certs/public.pem") final RSAPublicKey publicKey,
            @Value("classpath:certs/private.pem") final RSAPrivateKey privateKey) {
        this.rsa256 = Algorithm.RSA256(publicKey, privateKey);
        this.verifier = JWT.require(this.rsa256).build();
    }

    public String generateToken(final JwtUserDetails userDetails) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(userDetails.getUsername())
            .withPayload(
                new HashMap<>(){
                {
                    put("id", userDetails.getId());
                }
                {
                    put("username", userDetails.getUsername());
                }
            })
                // only for client information
                .withArrayClaim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .withIssuer("app")
                .withIssuedAt(now)
                .withExpiresAt(now.plusMillis(JWT_TOKEN_VALIDITY.toMillis()))
                .sign(this.rsa256);
    }

    public String validateTokenAndGetUsername(final String token) {
        try {
            return verifier.verify(token).getSubject();
        } catch (final JWTVerificationException verificationEx) {
            log.warn("token invalid: {}", verificationEx.getMessage());
            return null;
        }
    }

}
