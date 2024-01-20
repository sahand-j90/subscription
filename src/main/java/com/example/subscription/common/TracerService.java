package com.example.subscription.common;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Sahand Jalilvand 21.01.24
 */
@Component
@RequiredArgsConstructor
public class TracerService {

    private final Tracer tracer;

    public String getSpanId() {
        return currentSpan()
                .map(Span::context)
                .map(TraceContext::spanIdString)
                .orElse(null);
    }

    public String getTraceId() {
        return currentSpan()
                .map(Span::context)
                .map(TraceContext::traceIdString)
                .orElse(null);
    }

    public Long getSpanIdAsLong() {
        return currentSpan()
                .map(Span::context)
                .map(TraceContext::spanId)
                .orElse(null);
    }

    public Long getTraceIdAsLong() {
        return currentSpan()
                .map(Span::context)
                .map(TraceContext::traceId)
                .orElse(null);
    }

    public void injectTrace(long traceId, long spanId) {

        TraceContext traceContext = TraceContext.newBuilder()
                .traceId(traceId)
                .spanId(spanId)
                .build();

        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(traceContext)).start();
        tracer.withSpanInScope(span);
    }

    private Optional<Span> currentSpan() {
        return Optional.ofNullable(tracer.currentSpan());
    }

}
