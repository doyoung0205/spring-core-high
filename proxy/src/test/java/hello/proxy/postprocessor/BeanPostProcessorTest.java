package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanPostProcessorTest {

    @Test
    void basicConfig() {
        final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

        // B는 빈으로 등록된다.
        final B a = applicationContext.getBean("beanA", B.class);
        a.helloB();
        assertNotNull(a);
        assertThat(a).isInstanceOf(B.class);

        // A는 빈으로 등록되지 않는다.
        assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(BasicTest.A.class));
    }

    @Slf4j
    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        @Bean()
        public AToBPostProcessor aToBPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    /**
     * A 대신에 B를 스프링 컨테이너에 등록한다.
     */
    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("postProcessAfterInitialization() called with: bean = [" + bean + "], beanName = [" + beanName + "]");
            if (bean instanceof A) {
                return new B();
            }
            return bean;
        }
    }
}
