package hello.proxy.app.v3;

import hello.proxy.app.v1.OrderServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 implements OrderServiceV1 {

    private final OrderRepositoryV3 orderRepository;

    @Override
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
