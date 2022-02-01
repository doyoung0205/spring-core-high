package me.doyoung.springcorehigh.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import me.doyoung.springcorehigh.trace.strategy.code.strategy.ContextV2;
import me.doyoung.springcorehigh.trace.strategy.code.strategy.Strategy;
import me.doyoung.springcorehigh.trace.strategy.code.strategy.StrategyLogic1;
import me.doyoung.springcorehigh.trace.strategy.code.strategy.StrategyLogic2;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1() {
        final ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }
    /**
     * 전략 패턴 익명 내부 클래스
     */
    @Test
    void strategyV2() {
        final ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 1 실행");
            }
        });
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 2 실행");
            }
        });
    }

    /**
     * 전략 패턴 람다
     */
    @Test
    void strategyV3() {
        final ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직 1 실행"));
        context.execute(() -> log.info("비즈니스 로직 2 실행"));
    }
}
