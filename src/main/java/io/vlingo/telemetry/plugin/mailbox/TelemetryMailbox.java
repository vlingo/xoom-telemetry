// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Mailbox;
import io.vlingo.actors.Message;

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
  public void resume(final String name) {
    delegate.resume(name);
  }

  @Override
  public void send(final Message message) {
    try {
      delegate.send(new TelemetryMessage(message, telemetry));
      telemetry.onSendMessage(message);
    } catch (Exception e) {
      telemetry.onSendMessageFailed(message, e);
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
        telemetry.onReceiveMessage(receivedMessage);
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
