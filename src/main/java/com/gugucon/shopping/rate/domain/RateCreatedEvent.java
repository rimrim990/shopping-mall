package com.gugucon.shopping.rate.domain;

import com.gugucon.shopping.member.domain.vo.BirthYearRange;
import com.gugucon.shopping.member.domain.vo.Gender;
import com.gugucon.shopping.member.dto.MemberPrincipal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RateCreatedEvent {

    private final short score;
    private final long productId;
    private final BirthYearRange birthYearRange;
    private final Gender gender;

    public static RateCreatedEvent from(final MemberPrincipal principal, final short score, final long productId) {
        return new RateCreatedEvent(
            score,
            productId,
            BirthYearRange.from(principal.getBirthDate()),
            principal.getGender()
        );
    }
}
