package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("target={}", joinPoint.getTarget());
        log.info("getArgs={}", joinPoint.getArgs());
        log.info("getSignature={}", joinPoint.getSignature());

        TraceStatus status = null;
        try {
            final String message = joinPoint.getSignature().toShortString();
            // public void hello.proxy.app.v1.OrderRepositoryV1Impl.save(java.lang.String) - longString
            // OrderRepositoryV1Impl.save(..) - shortString
            status = logTrace.begin(message);
            // 로직 호출
            final Object result = joinPoint.proceed();
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
