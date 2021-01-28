package io.vlingo.telemetry.tracing;

import brave.RealSpan;
import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import io.vlingo.actors.Actor;
import io.vlingo.telemetry.plugin.mailbox.TelemetryMessage;

public final class TracingContext {
    private final RealSpan span;

    private TracingContext(final RealSpan span, final TelemetryMessage message, final Span.Kind spanKind, final RealSpan previousSpan) {
        this.span = span;
        if (span != null) {
            span.kind(spanKind);
            span.name(message.representation());
            span.tag("ActorAddress", message.actor().address().name());
            span.remoteServiceName(message.actor().getClass().getSimpleName());
            if (previousSpan != null) {
                span.localServiceName(previousSpan.remoteServiceName());
            } else {
                span.localServiceName(message.actor().getClass().getSimpleName());
            }
            span.start();
        }
    }

    public static TracingContext empty() {
        return new TracingContext(null, null, Span.Kind.SERVER, null);
    }

    public void flush() {
        if (span == null) {
            return;
        }

        span.finish();
        span.flush();
    }

    public void failed(Throwable ex) {
        if (span == null) {
            return;
        }

        span.error(ex);
        span.finish();
        span.flush();
    }

    public static TracingContext newContext(final Tracer tracer, final TelemetryMessage message) {
        if (message.actor().getClass().getCanonicalName().startsWith("io.vlingo") || message.representation().startsWith("start")) {
            return TracingContext.empty();
        }

        final Span span = tracer.newTrace();
        return new TracingContext((RealSpan) span, message, Span.Kind.SERVER, null);
    }

    public static TracingContext nextOf(final Tracer tracer, final TracingContext context, final TelemetryMessage message) {
        if (message.actor().getClass().getCanonicalName().startsWith("io.vlingo") || message.representation().startsWith("start")) {
            return TracingContext.empty();
        }

        context.flush();
        final Span nextSpan = tracer.nextSpan(TraceContextOrSamplingFlags.newBuilder(context.span.context()).build());
        return new TracingContext((RealSpan) nextSpan, message, Span.Kind.CLIENT, context.span);
    }

    public boolean isEmpty() {
        return span == null;
    }

    @Override
    public String toString() {
        return span == null ? "<empty>" : span.toString();
    }

}
