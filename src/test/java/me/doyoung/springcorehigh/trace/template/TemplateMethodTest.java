package me.doyoung.springcorehigh.trace.template;

import lombok.extern.slf4j.Slf4j;
import me.doyoung.springcorehigh.trace.template.code.AbstractTemplate;
import me.doyoung.springcorehigh.trace.template.code.SubClassLogic1;
import me.doyoung.springcorehigh.trace.template.code.SubClassLogic2;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {


    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        final long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        final long endTime = System.currentTimeMillis();
        final long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        final long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        final long endTime = System.currentTimeMillis();
        final long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }


    /**
     * 템플릿 메서드 패턴 적용
     */
    @Test
    void templateMethodV1() {
        AbstractTemplate template = new SubClassLogic1();
        template.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }
}
