package me.doyoung.springcorehigh.trace.logtrace;

/*
  쓰레드 로컬을 사용 할 떄는 <code>ThreadLocal.remove()</code> 를 통해서 꼭 제거해야 한다.
  쓰레드 로컬을 사용할 때는 이 부분을 꼭 기억 하자 !
 */

import lombok.extern.slf4j.Slf4j;
import me.doyoung.springcorehigh.trace.TraceId;
import me.doyoung.springcorehigh.trace.TraceStatus;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {

    private static final String START_PREFIX = "--->";
    private static final String COMPLETE_PREFIX = "<---";
    private static final String EX_PREFIX = "<x-";

    private final ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>(); // traceId 동기화

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        final TraceId traceId = traceIdHolder.get();
        final Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId() {
        final TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        final Long stopTimeMs = System.currentTimeMillis();
        final long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        final TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
        releaseTraceId();
    }

    private void releaseTraceId() {
        final TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove(); // 해당 스레드 전용 보관소가 비워짐
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|  ");
        }
        return sb.toString();
    }

}
