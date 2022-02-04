package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDiAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")
@Import(ProxyDiAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService;

    // spring.aop.proxy-target-class=false 일 경우
    // MemberService 를 구현한 com.sun.proxy.$Proxy50 가 넘어옴
    // 따라서 MemberServiceImpl 타입으로 의존관계 주입이 불가능하다.
    // 따라서 JDK 동적프록시를 사용할 경우 인터페이스 타입으로 받아야한다.
    @Autowired
    MemberServiceImpl memberServiceImpl;


    @Test
    void go() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
