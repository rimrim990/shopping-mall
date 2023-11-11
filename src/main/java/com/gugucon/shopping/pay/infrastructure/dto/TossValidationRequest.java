package com.gugucon.shopping.pay.infrastructure.dto;

import com.gugucon.shopping.pay.dto.request.PayRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class TossValidationRequest {

    private final String paymentKey;
    private final String encodedOrderId;
    private final Long amount;

    public static TossValidationRequest of(final PayRequest payRequest) {
        return new TossValidationRequest(payRequest.getPaymentKey(),
                                         payRequest.getOrderId(),
                                         payRequest.getAmount());
    }
}
