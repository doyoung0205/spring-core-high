package me.doyoung.springcorehigh.app.v5;

import me.doyoung.springcorehigh.trace.callback.TraceTemplate;
import me.doyoung.springcorehigh.trace.logtrace.LogTrace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV5 {
    private final OrderServiceV5 orderService;
    private final TraceTemplate traceTemplate;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.traceTemplate = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {
        return traceTemplate.execute("OrderController.request", () -> {
            orderService.orderItem(itemId);
            return "ok";
        });
    }
}
