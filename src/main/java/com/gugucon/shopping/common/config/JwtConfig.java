package com.gugucon.shopping.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private final String secretKey;
    private final Long expiration;
    private final Long refreshExpiration;

    public JwtConfig(final String secretKey, final Long expiration, final Long refreshExpiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
    }
}
