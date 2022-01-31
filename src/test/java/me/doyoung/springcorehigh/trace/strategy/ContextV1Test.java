package me.doyoung.springcorehigh.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import me.doyoung.springcorehigh.trace.strategy.code.ContextV1;
import me.doyoung.springcorehigh.trace.strategy.code.Strategy;
import me.doyoung.springcorehigh.trace.strategy.code.StrategyLogic1;
import me.doyoung.springcorehigh.trace.strategy.code.StrategyLogic2;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
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
     * 전략 패턴 사용
     */
    @Test
    void strategyV1() {
        final Strategy strategyLogic1 = new StrategyLogic1(); // 조립 1
        final ContextV1 contextV1 = new ContextV1(strategyLogic1); // 조립 2
        contextV1.execute();

        final Strategy strategyLogic2 = new StrategyLogic2();
        final ContextV1 contextV2 = new ContextV1(strategyLogic2);
        contextV2.execute();
    }
}
