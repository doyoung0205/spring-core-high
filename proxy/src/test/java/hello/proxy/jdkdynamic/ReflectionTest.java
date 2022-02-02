package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {


    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        final String result1 = target.callA();
        log.info("result={}", result1);
        // 공통 로직1 종료


        // 공통 로직2 시작
        log.info("start");
        final String result2 = target.callB();
        log.info("result={}", result2);
        // 공통 로직2 종료


        // target.callA 와 target.callB 만 동적으로 처리하면 중복을 많이 줄일 수 있을 것이다.
        // -> 리플렉션 이용
        // cf). 람다를 사용해서 동적으로 호출하는 방법도 있음.
    }

    @Test
    void reflection1() throws Exception {
        // 클래스 정보를 획득.
        final Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();
        // Get callA method info
        Method methodCallA = classHello.getMethod("callA");
        final Object result1 = methodCallA.invoke(target);
        log.info("result1={}", result1);

        // Get callB method info
        Method methodCallB = classHello.getMethod("callB");
        final Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);
    }

    @Test
    void reflection2() throws Exception {
        // 클래스 정보를 획득.
        final Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();

        // Get callA method info
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        // Get callB method info
        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    /**
     * 공통 로직 사이에 비즈니스 로직을 Method 로 추상화 하여
     * 통합된 공통 로직 처리를 한다.
     */
    private void dynamicCall(Method method, Object object) throws Exception {
        log.info("start");
        final Object result = method.invoke(object);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}
