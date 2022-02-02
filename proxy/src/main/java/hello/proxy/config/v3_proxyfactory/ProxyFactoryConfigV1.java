package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderRepositoryV1Impl;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.app.v1.OrderServiceV1Impl;
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
public class ProxyFactoryConfigV1 {


    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        final OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1(logTrace));
        final ProxyFactory proxyFactory = new ProxyFactory(orderController);
        proxyFactory.addAdvice(getAdvisor(logTrace));
        final OrderControllerV1 proxy = (OrderControllerV1) proxyFactory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderController.getClass());
        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        final OrderServiceV1Impl orderService = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        final ProxyFactory proxyFactory = new ProxyFactory(orderService);
        proxyFactory.addAdvice(getAdvisor(logTrace));
        final OrderServiceV1 proxy = (OrderServiceV1) proxyFactory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderService.getClass());
        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        final OrderRepositoryV1Impl orderRepository = new OrderRepositoryV1Impl();
        final ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
        proxyFactory.addAdvice(getAdvisor(logTrace));
        final OrderRepositoryV1 proxy = (OrderRepositoryV1) proxyFactory.getProxy();
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
