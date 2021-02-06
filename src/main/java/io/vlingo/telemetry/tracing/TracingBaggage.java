package io.vlingo.telemetry.tracing;

import brave.Tracer;
import io.vlingo.telemetry.plugin.mailbox.TelemetryMessage;

public final class TracingBaggage {
    private final ThreadLocal<TracingContext> currentContext;
    private final ThreadLocal<TelemetryMessage> previousMessage;
    private final Tracer tracer;

    private TracingBaggage(final Tracer tracer) {
        this.tracer = tracer;
        this.currentContext = ThreadLocal.withInitial(TracingContext::empty);
        this.previousMessage = ThreadLocal.withInitial(() -> new TelemetryMessage(null, null, null));
    }

    public static TracingBaggage withTracer(final Tracer tracer) {
        return new TracingBaggage(tracer);
    }

    public TracingContext baggageState() {
        return currentContext.get();
    }

    public TracingContext storeInBaggage(final TelemetryMessage message) {
        if (message.tracingContext() == null || message.tracingContext().isEmpty()) {
            final TracingContext tracingContext = TracingContext.newContext(tracer, message);
            currentContext.set(tracingContext);
            previousMessage.set(message);
            return tracingContext;
        }

        final TracingContext tracingContext = TracingContext.nextOf(tracer, message.tracingContext(), message);
        currentContext.set(tracingContext);
        return tracingContext;
    }
}
