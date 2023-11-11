package com.gugucon.shopping.pay.service;

import com.gugucon.shopping.pay.dto.request.PayRequest;

public interface PayValidator {

    void validatePayment(final PayRequest payRequest);
}
