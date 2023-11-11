package com.gugucon.shopping.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pay.callback")
public class TossPayConfig {

    private String successUrl;
    private String failUrl;
}
