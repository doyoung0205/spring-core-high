package hello.proxy.advisor;

import hello.proxy.common.ServiceImpl;
import hello.proxy.common.ServiceInterface;
import hello.proxy.common.advice.TimeAdvice;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {
    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        final ProxyFactory proxyFactory = new ProxyFactory(target);
        final DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(
                Pointcut.TRUE, new TimeAdvice()
        );
        proxyFactory.addAdvisor(advisor);
        final ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }

    @Test
    void advisorTest2() {
        ServiceInterface target = new ServiceImpl();
        final ProxyFactory proxyFactory = new ProxyFactory(target);
        final DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(
                new MyPointCut(), new TimeAdvice()
        );
        proxyFactory.addAdvisor(advisor);
        final ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }

    static class MyPointCut implements Pointcut {

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    static class MyMethodMatcher implements MethodMatcher {

        private String matchName = "save";

        /**
         * isRuntime 이 false 일 때 실행됨
         * 캐싱을 하는 장점이 있음
         */
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            final boolean result = method.getName().equals(matchName);
            log.info("포인트컷 호출 method={} targetClass={}", method.getName(), targetClass);
            log.info("포인트컷 결과 result={}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        /**
         * isRuntime 가 true 일 경우 실행
         * 매개변수가 동적으로 변경된다고 가정하기 때문에 캐싱을 하지 않는다.
         */
        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }


    @Test
    void advisorTest3() {
        ServiceInterface target = new ServiceImpl();
        final ProxyFactory proxyFactory = new ProxyFactory(target);
        final NameMatchMethodPointcut pointCut = new NameMatchMethodPointcut();
        pointCut.setMappedName("save");
        final DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(
                pointCut, new TimeAdvice()
        );
        proxyFactory.addAdvisor(advisor);
        final ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }

}
