package me.doyoung.springcorehigh.app.template.v4;

import lombok.RequiredArgsConstructor;
import me.doyoung.springcorehigh.trace.logtrace.LogTrace;
import me.doyoung.springcorehigh.trace.template.AbstractTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {
    private final OrderRepositoryV4 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {
        final AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {

            @Override
            public Void call() {
                orderRepository.save(itemId);
                return null;
            }
        };
        template.execute("OrderService.orderItem()");
    }
}
