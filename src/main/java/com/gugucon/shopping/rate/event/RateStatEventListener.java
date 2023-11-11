package com.gugucon.shopping.rate.event;

import com.gugucon.shopping.rate.domain.RateCreatedEvent;
import com.gugucon.shopping.rate.repository.RateStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Transactional
@RequiredArgsConstructor
public class RateStatEventListener {

    private final RateStatRepository rateStatRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = RateCreatedEvent.class)
    public void handleEvent(final RateCreatedEvent event) {
        rateStatRepository.updateRateStatByScore(event.getScore(),
            event.getProductId(),
            event.getBirthYearRange(),
            event.getGender());
    }
}
