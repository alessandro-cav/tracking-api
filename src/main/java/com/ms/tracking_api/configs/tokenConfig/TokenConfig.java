package com.ms.tracking_api.configs.tokenConfig;


import com.ms.tracking_api.dtos.responses.TokenInfo;
import com.ms.tracking_api.handlers.BadRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Configuration
public class TokenConfig {

    private static final String SECRET_KEY = "l5sdxk7WnLZqI3jl4rE9ABK7Wxy4TX7GJuCw1v/RWlM=";
    private static final long EXPIRATION_TIME = 300000; // 5 minutos em milissegundos

    private static Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(Long idFuncionario, String tipoAtividade, Long idVaga) {
        return Jwts.builder()
                .claim("idFuncionario", idFuncionario.toString())
                .claim("tipoAtividade", tipoAtividade)
                .claim("idVaga", idVaga.toString())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenInfo validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String idFuncionario = claims.get("idFuncionario", String.class);
            String tipoAtividade = claims.get("tipoAtividade", String.class);
            String idVaga = claims.get("idVaga", String.class);
            return new TokenInfo(Long.parseLong(idFuncionario), tipoAtividade, Long.parseLong(idVaga));
        } catch (Exception e) {
            throw new BadRequestException("Token inválido ou expirado");
        }
    }
}



