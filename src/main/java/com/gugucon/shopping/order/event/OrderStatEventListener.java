package com.gugucon.shopping.order.event;

import com.gugucon.shopping.order.domain.OrderCompletedEvent;
import com.gugucon.shopping.order.repository.OrderStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Transactional
@RequiredArgsConstructor
public class OrderStatEventListener {

    private final OrderStatRepository orderStatRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = OrderCompletedEvent.class)
    public void updateCount(final OrderCompletedEvent event) {
        orderStatRepository.updateOrderStatByCount(event.getCount(),
            event.getProductId(),
            event.getBirthYearRange(),
            event.getGender());
    }
}
