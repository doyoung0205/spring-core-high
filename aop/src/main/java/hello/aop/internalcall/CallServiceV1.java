package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    // 빈 생성을 다하고 생성된 자기 자신을 setter 로 주입을 받는다.
    @Autowired
    public void setCallServiceV1(CallServiceV1  callServiceV1) {
        log.info("callServiceV1 setter={}", callServiceV1.getClass());
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); // 외부 메서드 호출(callServiceV1.internal())
    }

    public void internal() {
        log.info("call internal");
    }
}
