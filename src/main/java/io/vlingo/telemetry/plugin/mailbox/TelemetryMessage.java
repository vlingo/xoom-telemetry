// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Message;
import io.vlingo.actors.Returns;
import io.vlingo.common.SerializableConsumer;
import io.vlingo.telemetry.tracing.TracingBaggage;
import io.vlingo.telemetry.tracing.TracingContext;

public class TelemetryMessage implements Message {
  private final Message delegate;
  private final MailboxTelemetry telemetry;
  private final TracingBaggage tracingBaggage;
  private final TracingContext tracingContext;

  public TelemetryMessage(final Message delegate, final MailboxTelemetry telemetry, final TracingBaggage tracingBaggage) {
    this.delegate = delegate;
    this.telemetry = telemetry;
    this.tracingBaggage = tracingBaggage;
    this.tracingContext = tracingBaggage != null ? tracingBaggage.baggageState() : null;
  }

  @Override
  public Actor actor() {
    return delegate.actor();
  }

  public TracingContext tracingContext() {
    return tracingContext;
  }

  @Override
  public void deliver() {
    TracingContext nextTracingContext = tracingBaggage.storeInBaggage(this);
    try {
      delegate.deliver();
      telemetry.onReceiveMessage(delegate.actor());
    } catch (RuntimeException ex) {
      telemetry.onDeliverMessageFailed(delegate.actor(), ex);
      nextTracingContext.failed(ex);
    } finally {
      nextTracingContext.flush();
    }
  }

  @Override
  public Class<?> protocol() {
    return delegate.protocol();
  }

  @Override
  public String representation() {
    return delegate.representation();
  }

  @Override
  public boolean isStowed() {
    return delegate.isStowed();
  }

  @Override
  public void set(final Actor actor, final Class<?> protocol, final SerializableConsumer<?> consumer, final Returns<?> returns, final String representation) {
    delegate.set(actor, protocol, consumer, returns, representation);
  }
}
