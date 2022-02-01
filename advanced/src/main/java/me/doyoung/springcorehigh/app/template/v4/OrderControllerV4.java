package me.doyoung.springcorehigh.app.template.v4;

import lombok.RequiredArgsConstructor;
import me.doyoung.springcorehigh.trace.logtrace.LogTrace;
import me.doyoung.springcorehigh.trace.template.AbstractTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {
    private final OrderServiceV4 orderService;
    private final LogTrace trace;

    @GetMapping("/v4/request")
    public String request(String itemId) {

        final AbstractTemplate<String> template = new AbstractTemplate<>(trace) {
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };
        return template.execute("OrderController.request");
    }
}
