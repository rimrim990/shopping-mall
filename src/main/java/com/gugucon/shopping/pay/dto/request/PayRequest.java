package com.gugucon.shopping.pay.dto.request;

public interface PayRequest {

    String getPaymentKey();
    String getOrderId();
    Long getAmount();
    String getPaymentType();
}
