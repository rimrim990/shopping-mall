package com.gugucon.shopping.pay.infrastructure;

import com.gugucon.shopping.pay.service.CustomerKeyGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DefaultCustomerKeyGenerator implements CustomerKeyGenerator {

    @Override
    public String generate(final Long value) {
        return UUID.nameUUIDFromBytes(String.valueOf(value).getBytes()).toString();
    }
}
