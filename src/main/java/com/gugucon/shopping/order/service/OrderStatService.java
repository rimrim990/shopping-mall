package com.gugucon.shopping.order.service;

import com.gugucon.shopping.member.domain.vo.BirthYearRange;
import com.gugucon.shopping.member.dto.MemberPrincipal;
import com.gugucon.shopping.order.domain.entity.OrderItem;
import com.gugucon.shopping.order.repository.OrderStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderStatService {

    private final OrderStatRepository orderStatRepository;

    public void updateCount(final MemberPrincipal principal, final OrderItem orderItem) {
        orderStatRepository.updateOrderStatByCount(orderItem.getQuantity().getValue(),
            orderItem.getProductId(),
            BirthYearRange.from(principal.getBirthDate()),
            principal.getGender());
    }
}
