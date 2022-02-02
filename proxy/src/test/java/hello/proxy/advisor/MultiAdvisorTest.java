package hello.proxy.advisor;

import hello.proxy.common.ServiceImpl;
import hello.proxy.common.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class MultiAdvisorTest {

    /**
     * 어드바이저 마다 10개라면 10개의 프록시를 생성해야하는 단점이 있다.
     */
    @Test
    void multiAdvisorTest1() {
        // client -> proxy2(advisor2) -> proxy1(advisor1) -> target

        // proxy1 생성
        ServiceInterface target = new ServiceImpl();
        final ProxyFactory proxyFactory1 = new ProxyFactory(target);
        final DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(
                Pointcut.TRUE, new Advice1()
        );
        proxyFactory1.addAdvisor(advisor1);
        final ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();


        // proxy2 생성 target -> proxy1
        final ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        final DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(
                Pointcut.TRUE, new Advice2()
        );
        proxyFactory2.addAdvisor(advisor2);
        final ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        //
        proxy2.find();
        proxy2.save();


    }

    @Slf4j
    static class Advice1 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    @Slf4j
    static class Advice2 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }

    @Test
    @DisplayName("하나의 프록시, 여러 어드바이저")
    void multiAdvisorTest2() {
        // client -> proxy -> advisor2 -> advisor1 -> target
        // proxy1 생성
        final DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(
                Pointcut.TRUE, new Advice1()
        );
        final DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(
                Pointcut.TRUE, new Advice2()
        );

        ServiceInterface target = new ServiceImpl();
        final ProxyFactory proxyFactory1 = new ProxyFactory(target);
        // 등록하는 순서대로 어드바이스가 실행된다.
        proxyFactory1.addAdvisor(advisor2);
        proxyFactory1.addAdvisor(advisor1);
        final ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        proxy1.save();
    }
}
