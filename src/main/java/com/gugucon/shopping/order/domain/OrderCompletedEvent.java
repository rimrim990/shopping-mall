package com.gugucon.shopping.order.domain;

import com.gugucon.shopping.member.domain.vo.BirthYearRange;
import com.gugucon.shopping.member.domain.vo.Gender;
import com.gugucon.shopping.member.dto.MemberPrincipal;
import com.gugucon.shopping.order.domain.entity.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCompletedEvent {

    private final int count;
    private final long productId;
    private final BirthYearRange birthYearRange;
    private final Gender gender;

    public static OrderCompletedEvent from(final MemberPrincipal principal, final OrderItem orderItem) {
        return new OrderCompletedEvent(
            orderItem.getQuantity().getValue(),
            orderItem.getProductId(),
            BirthYearRange.from(principal.getBirthDate()),
            principal.getGender()
        );
    }
}
