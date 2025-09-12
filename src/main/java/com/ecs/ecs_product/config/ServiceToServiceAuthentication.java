package com.ecs.ecs_product.config;

import com.ecs.ecs_product.util.ExtractSecrets;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ServiceToServiceAuthentication {
    private final SecretKey signingKey;
    private final String serviceClientId;
    private final AtomicReference<String> cachedToken = new AtomicReference<>();
    private volatile long expiryMillis = 0L;

    public ServiceToServiceAuthentication() {
        byte[] keyBytes = Decoders.BASE64.decode(ExtractSecrets.getSecret("USER_SECRET_KEY"));
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.serviceClientId = ExtractSecrets.getSecret("SERVICE_CLIENT_ID");
    }

    public synchronized String getToken() {
        long now = System.currentTimeMillis();
        if (cachedToken.get() != null && now < expiryMillis - 500L) {
            return cachedToken.get();
        }
        long ttl = Long.parseLong(ExtractSecrets.getSecret("SERVICE_TOKEN_TTL_SECONDS")) * 1000L;
        Date exp = new Date(now + ttl);
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "service");
        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(serviceClientId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(exp)
                .and()
                .signWith(signingKey)
                .compact();

        cachedToken.set(token);
        expiryMillis = exp.getTime();
        return token;
    }

    public synchronized void invalidate(){ cachedToken.set(null); expiryMillis = 0L; }

}
