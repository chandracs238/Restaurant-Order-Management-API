package com.pcs.restaurantapi.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "849e61c8fec4fd3879b48f35d7e37050bcdd1d54d66113bce96821c85f32620506f5f29508da0e3e3994db4b51a1ba30d9884b84ace2e980ccd0d9a8fa7eb186ceb1d7b2e3abe226bdc61f11ab95c0a1ac2d22d03d1ba1b29545a02170a8f1da0e6a69a7e00be20d1abbca44027a44165e87804ccdaa7cc2f3f5bff3bc0b2f7afecf4f90ba9ac5e42f49282cc77c07e8a87ca85cee6c4aad2944c38f825615759b0d013f00f92c2e1f04f749fc6f309dfa4c8b0433203273065f211a088b642886b5f54a4bf86850ebd51bb36ef5b71b4bbab65b19e2af3ef237c0b5337de7d2d30f44ad34fd364caf99b840890fb542a5f47ba08815a8d354f8a895fd345ab1";  // Use a strong secret key
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generate JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate Token
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extract Username from Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Check if Token is Expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract Expiration Date from Token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract any Claim from Token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse JWT and Extract All Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

