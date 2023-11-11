package com.gugucon.shopping.rate.service;

import com.gugucon.shopping.member.domain.vo.BirthYearRange;
import com.gugucon.shopping.member.dto.MemberPrincipal;
import com.gugucon.shopping.rate.repository.RateStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RateStatService {

    private final RateStatRepository rateStatRepository;

    public void updateRate(final MemberPrincipal principal, final short score, final Long productId) {
        rateStatRepository.updateRateStatByScore(score,
            productId,
            BirthYearRange.from(principal.getBirthDate()),
            principal.getGender());
    }
}
