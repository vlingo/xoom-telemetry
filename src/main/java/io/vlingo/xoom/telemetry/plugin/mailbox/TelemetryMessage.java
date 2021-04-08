// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry.plugin.mailbox;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Message;
import io.vlingo.xoom.actors.Returns;
import io.vlingo.xoom.common.SerializableConsumer;

public class TelemetryMessage implements Message {
  private final Message delegate;
  private final MailboxTelemetry telemetry;

  public TelemetryMessage(final Message delegate, final MailboxTelemetry telemetry) {
    this.delegate = delegate;
    this.telemetry = telemetry;
  }

  @Override
  public Actor actor() {
    return delegate.actor();
  }

  @Override
  public void deliver() {
    try {
      delegate.deliver();
      telemetry.onReceiveMessage(delegate.actor());
    } catch (RuntimeException ex) {
      telemetry.onDeliverMessageFailed(delegate.actor(), ex);
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
