package com.gugucon.shopping.pay.service;

import com.gugucon.shopping.common.domain.vo.Money;
import com.gugucon.shopping.common.exception.ErrorCode;
import com.gugucon.shopping.common.exception.ShoppingException;
import com.gugucon.shopping.member.dto.MemberPrincipal;
import com.gugucon.shopping.order.domain.entity.Order;
import com.gugucon.shopping.order.service.OrderService;
import com.gugucon.shopping.pay.config.TossPayConfig;
import com.gugucon.shopping.pay.domain.Pay;
import com.gugucon.shopping.pay.dto.request.PointPayRequest;
import com.gugucon.shopping.pay.dto.request.TossPayFailRequest;
import com.gugucon.shopping.pay.dto.request.TossPayRequest;
import com.gugucon.shopping.pay.dto.response.PayResponse;
import com.gugucon.shopping.pay.dto.response.TossPayFailResponse;
import com.gugucon.shopping.pay.dto.response.TossPayInfoResponse;
import com.gugucon.shopping.pay.repository.PayRepository;
import com.gugucon.shopping.point.domain.Point;
import com.gugucon.shopping.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final PointRepository pointRepository;
    private final PayValidator payValidator;
    private final OrderIdTranslator orderIdTranslator;
    private final CustomerKeyGenerator customerKeyGenerator;
    private final TossPayConfig tossPayConfig;
    private final OrderService orderService;

    @Transactional
    public PayResponse payByPoint(final PointPayRequest pointPayRequest, final MemberPrincipal principal) {
        final Long orderId = pointPayRequest.getOrderId();
        final Long memberId = principal.getId();
        final Order order = orderService.findOrderByExclusively(orderId, memberId);
        order.validateNotCanceled();

        final Point point = findPointBy(memberId);
        point.use(order.calculateTotalPrice());
        orderService.complete(order, principal);

        return PayResponse.from(payRepository.save(Pay.from(order)));
    }

    private Point findPointBy(final Long memberId) {
        return pointRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ShoppingException(ErrorCode.POINT_NOT_ENOUGH));
    }

    public TossPayInfoResponse getTossInfo(final Long orderId, final Long memberId) {
        final Order order = orderService.findOrderBy(orderId, memberId);
        order.validateNotCanceled();

        return TossPayInfoResponse.from(orderIdTranslator.encode(order.getId(), order.createOrderName()),
                                        order,
                                        customerKeyGenerator.generate(memberId),
                                        tossPayConfig.getSuccessUrl(),
                                        tossPayConfig.getFailUrl());
    }

    @Transactional
    public PayResponse payByToss(final TossPayRequest tossPayRequest, final MemberPrincipal principal) {
        final Long orderId = orderIdTranslator.decode(tossPayRequest.getOrderId());
        final Long memberId = principal.getId();
        final Order order = orderService.findOrderByExclusively(orderId, memberId);

        order.validateNotCanceled();
        order.validateMoney(Money.from(tossPayRequest.getAmount()));
        orderService.complete(order, principal);
        final Pay pay = payRepository.save(Pay.from(order));
        payValidator.validatePayment(tossPayRequest);

        return PayResponse.from(pay);
    }

    public TossPayFailResponse decodeOrderId(final TossPayFailRequest tossPayFailRequest) {
        final Long orderId = orderIdTranslator.decode(tossPayFailRequest.getOrderId());
        return TossPayFailResponse.from(orderId);
    }
}
