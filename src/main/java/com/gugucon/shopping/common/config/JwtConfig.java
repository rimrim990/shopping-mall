package com.gugucon.shopping.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private final String secretKey;
    private final Long expiration;

    public JwtConfig(final String secretKey, final Long expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }
}
