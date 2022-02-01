package me.doyoung.springcorehigh.trace.logtrace;

import lombok.extern.slf4j.Slf4j;
import me.doyoung.springcorehigh.trace.TraceId;
import me.doyoung.springcorehigh.trace.TraceStatus;

@Slf4j
public class FiledLogTrace implements LogTrace {

    private static final String START_PREFIX = "--->";
    private static final String COMPLETE_PREFIX = "<---";
    private static final String EX_PREFIX = "<x-";

    private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생
    // 싱글톤 객체 에서 상태를 변경할 수 있는 상황일 떄 동시성 이슈 발생

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        final TraceId traceId = traceIdHolder;
        final Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }
    private void syncTraceId() {
        if (traceIdHolder == null) {
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNextId();
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
        if (traceIdHolder.isFirstLevel()) {
            traceIdHolder = null; // destroy
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
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
