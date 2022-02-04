package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV2Test {

    @Autowired
    CallServiceV2 callServiceV2;

    @Test
    void external() {
        log.info("target={}", callServiceV2.getClass());
        // 해당 메서드 안에 있는 internal 도 포인트컷에 걸리는데 어디바이스가 적용되지 않음.
        // 프록시의 internal 을 호출하지 않고, 타겟 객체의 internal 을 호출 하기 때문.
        callServiceV2.external();
    }

    @Test
    void internal() {
        callServiceV2.internal();
    }
}
