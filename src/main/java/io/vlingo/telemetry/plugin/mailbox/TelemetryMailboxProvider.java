// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Dispatcher;
import io.vlingo.actors.Mailbox;
import io.vlingo.actors.MailboxProvider;
import io.vlingo.telemetry.tracing.TracingBaggage;

public class TelemetryMailboxProvider implements MailboxProvider {
  private final MailboxTelemetry telemetry;
  private final MailboxProvider delegate;
  private final TracingBaggage baggage;

  public TelemetryMailboxProvider(final MailboxTelemetry telemetry, final MailboxProvider delegate, final TracingBaggage baggage) {
    this.telemetry = telemetry;
    this.delegate = delegate;
    this.baggage = baggage;
  }

  @Override
  public void close() {
    delegate.close();
  }

  @Override
  public Mailbox provideMailboxFor(final int hashCode) {
    return new TelemetryMailbox(telemetry, delegate.provideMailboxFor(hashCode), baggage);
  }

  @Override
  public Mailbox provideMailboxFor(final int hashCode, final Dispatcher dispatcher) {
    return new TelemetryMailbox(telemetry, delegate.provideMailboxFor(hashCode, dispatcher), baggage);
  }
}
