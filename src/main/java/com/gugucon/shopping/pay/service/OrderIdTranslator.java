package com.gugucon.shopping.pay.service;

public interface OrderIdTranslator {

    String encode(final Long orderId, final String orderName);

    Long decode(final String encodedOrderId);
}
