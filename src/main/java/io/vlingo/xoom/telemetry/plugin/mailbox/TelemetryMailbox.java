// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry.plugin.mailbox;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Mailbox;
import io.vlingo.xoom.actors.Message;
import io.vlingo.xoom.actors.Returns;
import io.vlingo.xoom.common.SerializableConsumer;

public class TelemetryMailbox implements Mailbox {
  private final MailboxTelemetry telemetry;
  private final Mailbox delegate;

  public TelemetryMailbox(final MailboxTelemetry telemetry, final Mailbox delegate) {
    this.telemetry = telemetry;
    this.delegate = delegate;
  }

  @Override
  public void close() {
    delegate.close();
  }

  @Override
  public boolean isClosed() {
    return delegate.isClosed();
  }

  @Override
  public boolean isDelivering() {
    return delegate.isDelivering();
  }

  @Override
  public int concurrencyCapacity() {
    return delegate.concurrencyCapacity();
  }

  @Override
  public void resume(final String name) {
    delegate.resume(name);
  }

  @Override
  public void send(final Message message) {
    try {
      delegate.send(new TelemetryMessage(message, telemetry));
      telemetry.onSendMessage(message.actor());
    } catch (Exception e) {
      telemetry.onSendMessageFailed(message.actor(), e);
    }
  }

  @Override
  public boolean isPreallocated() {
    return delegate.isPreallocated();
  }

  @Override
  public void send(Actor actor, Class<?> protocol, SerializableConsumer<?> consumer, Returns<?> returns, String representation) {
    try {
      delegate.send(actor, protocol, consumer, returns, representation);
      telemetry.onSendMessage(actor);
    } catch (Exception e) {
      telemetry.onSendMessageFailed(actor, e);
    }
  }

  @Override
  public void suspendExceptFor(final String name, final Class<?>... overrides) {
    delegate.suspendExceptFor(name, overrides);
  }

  @Override
  public boolean isSuspended() {
    return delegate.isSuspended();
  }

  @Override
  public Message receive() {
    try {
      Message receivedMessage = delegate.receive();
      if (receivedMessage == null) {
        telemetry.onReceiveEmptyMailbox();
      } else {
        telemetry.onReceiveMessage(receivedMessage.actor());
      }

      return receivedMessage;
    } catch (RuntimeException e) {
      telemetry.onReceiveMessageFailed(e);
      throw e;
    }
  }

  @Override
  public int pendingMessages() {
    return delegate.pendingMessages();
  }

  @Override
  public void run() {
    delegate.run();
  }
}
