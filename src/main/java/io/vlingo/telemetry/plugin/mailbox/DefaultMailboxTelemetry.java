package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Message;
import io.vlingo.telemetry.Telemetry;

public class DefaultMailboxTelemetry implements MailboxTelemetry {
  public static final String PREFIX = "vlingo.MailboxTelemetry.";

  public static final String SCOPE_INSTANCE = "Instance";
  public static final String SCOPE_CLASS = "Class";

  public static final String PENDING = "pending";
  public static final String IDLE = "idle";
  public static final String FAILED_SEND = "failed.send";
  public static final String FAILED_DELIVER = "failed.deliver";

  private final Telemetry<?> telemetry;

  public DefaultMailboxTelemetry(final Telemetry<?> telemetry) {
    this.telemetry = telemetry;
  }

  @Override
  public void onSendMessage(final Message message) {
    incrementGaugeFor(message, 1, PENDING);
  }

  @Override
  public void onSendMessageFailed(final Message message, final Throwable exception) {
    Class<? extends Throwable> exceptionClass = exception.getClass();
    String exceptionName = exceptionClass.getSimpleName();

    incrementCounterFor(message, FAILED_SEND + "." + exceptionName);
    incrementCounterFor(exceptionClass);
  }

  @Override
  public void onReceiveEmptyMailbox() {
    incrementIdleCounter();
  }

  @Override
  public void onReceiveMessage(final Message message) {
    incrementGaugeFor(message, -1, PENDING);
  }

  @Override
  public void onReceiveMessageFailed(final Throwable exception) {
    incrementCounterFor(exception.getClass());
  }

  @Override
  public void onDeliverMessageFailed(final Message message, final Throwable exception) {
    Class<? extends Throwable> exceptionClass = exception.getClass();
    String exceptionName = exceptionClass.getSimpleName();

    incrementCounterFor(message, FAILED_DELIVER + "." + exceptionName);
    incrementCounterFor(exceptionClass);
  }

  public final void incrementGaugeFor(final Message message, final int delta, final String concept) {
    String gaugeName = PREFIX + message.actor().getClass().getSimpleName() + "." + concept;
    telemetry.gauge(gaugeName, delta, Telemetry.Tag.of("Address", message.actor().address().name()));
  }

  public final void incrementCounterFor(final Message message, final String concept) {
    String gaugeName = PREFIX + message.actor().getClass().getSimpleName() + "." + concept;
    telemetry.count(gaugeName, 1, Telemetry.Tag.of("Address", message.actor().address().name()));
  }

  public final void incrementCounterFor(final Class<? extends Throwable> ex) {
    String exceptionName = ex.getSimpleName();
    telemetry.count(PREFIX + exceptionName, 1);
  }

  public final void incrementIdleCounter() {
    telemetry.count(PREFIX + IDLE, 1);
  }
}
