package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class ProxyCastingTest {
    @Test
    void jdkProxy() {
        final MemberServiceImpl target = new MemberServiceImpl();
        final ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시

        // 프록시를 인터페이스로 캐스팅 성공
        final MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        assertThrows(ClassCastException.class, () -> {
            final MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });
    }

    @Test
    void cglibProxy() {
        final MemberServiceImpl target = new MemberServiceImpl();
        final ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // CGLIB 프록시

        // 프록시를 인터페이스로 캐스팅 성공
        final MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // CGLIB 프록시를 구현 클래스로 캐스팅 시도 성공
        assertDoesNotThrow(() -> {
            final MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });
    }
}
