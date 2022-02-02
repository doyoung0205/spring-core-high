package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace logTrace) {
        final OrderControllerV2 orderController = new OrderControllerV2(orderServiceV2(logTrace));
        final ProxyFactory proxyFactory = new ProxyFactory(orderController);
        proxyFactory.addAdvice(getAdvisor(logTrace));
        final OrderControllerV2 proxy = (OrderControllerV2) proxyFactory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderController.getClass());
        return proxy;
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace logTrace) {
        final OrderServiceV2 orderService = new OrderServiceV2(orderRepositoryV2(logTrace));
        final ProxyFactory proxyFactory = new ProxyFactory(orderService);
        proxyFactory.addAdvice(getAdvisor(logTrace));
        final OrderServiceV2 proxy = (OrderServiceV2) proxyFactory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderService.getClass());
        return proxy;
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace) {
        final OrderRepositoryV2 orderRepository = new OrderRepositoryV2();
        final ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
        proxyFactory.addAdvice(getAdvisor(logTrace));
        final OrderRepositoryV2 proxy = (OrderRepositoryV2) proxyFactory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderRepository.getClass());
        return proxy;
    }

    private Advice getAdvisor(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        final LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return advice;
    }
}
