package me.doyoung.springcorehigh.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

    public void execute() {
        final long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        call(); // 오버라이딩 된 서브 클래스 로직 call 이 실행된다.
        // 비즈니스 로직 종료
        final long endTime = System.currentTimeMillis();
        final long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    protected abstract void call();
}
