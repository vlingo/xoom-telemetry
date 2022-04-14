// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry.plugin.mailbox;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.telemetry.Telemetry;

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
  public void onSendMessage(final Actor actor) {
    incrementGaugeFor(actor, 1, PENDING);
  }

  @Override
  public void onSendMessageFailed(final Actor actor, final Throwable exception) {
    Class<? extends Throwable> exceptionClass = exception.getClass();
    String exceptionName = exceptionClass.getSimpleName();

    incrementCounterFor(actor, FAILED_SEND + "." + exceptionName);
    incrementCounterFor(exceptionClass);
  }

  @Override
  public void onReceiveEmptyMailbox() {
    incrementIdleCounter();
  }

  @Override
  public void onReceiveMessage(final Actor actor) {
    incrementGaugeFor(actor, -1, PENDING);
  }

  @Override
  public void onReceiveMessageFailed(final Throwable exception) {
    incrementCounterFor(exception.getClass());
  }

  @Override
  public void onDeliverMessageFailed(final Actor actor, final Throwable exception) {
    Class<? extends Throwable> exceptionClass = exception.getClass();
    String exceptionName = exceptionClass.getSimpleName();

    incrementCounterFor(actor, FAILED_DELIVER + "." + exceptionName);
    incrementCounterFor(exceptionClass);
  }

  public final void incrementGaugeFor(final Actor actor, final int delta, final String concept) {
    String gaugeName = PREFIX + actor.getClass().getSimpleName() + "." + concept;
    telemetry.gauge(gaugeName, delta, Telemetry.Tag.of("Address", actor.address().name()));
  }

  public final void incrementCounterFor(final Actor actor, final String concept) {
    String gaugeName = PREFIX + actor.getClass().getSimpleName() + "." + concept;
    telemetry.count(gaugeName, 1, Telemetry.Tag.of("Address", actor.address().name()));
  }

  public final void incrementCounterFor(final Class<? extends Throwable> ex) {
    String exceptionName = ex.getSimpleName();
    telemetry.count(PREFIX + exceptionName, 1);
  }

  public final void incrementIdleCounter() {
    telemetry.count(PREFIX + IDLE, 1);
  }
}
