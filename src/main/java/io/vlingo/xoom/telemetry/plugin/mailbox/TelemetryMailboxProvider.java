// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry.plugin.mailbox;

import io.vlingo.xoom.actors.Dispatcher;
import io.vlingo.xoom.actors.Mailbox;
import io.vlingo.xoom.actors.MailboxProvider;

public class TelemetryMailboxProvider implements MailboxProvider {
  private final MailboxTelemetry telemetry;
  private final MailboxProvider delegate;

  public TelemetryMailboxProvider(final MailboxTelemetry telemetry, final MailboxProvider delegate) {
    this.telemetry = telemetry;
    this.delegate = delegate;
  }

  @Override
  public void close() {
    delegate.close();
  }

  @Override
  public Mailbox provideMailboxFor(final int hashCode) {
    return new TelemetryMailbox(telemetry, delegate.provideMailboxFor(hashCode));
  }

  @Override
  public Mailbox provideMailboxFor(final int hashCode, final Dispatcher dispatcher) {
    return new TelemetryMailbox(telemetry, delegate.provideMailboxFor(hashCode, dispatcher));
  }
}
