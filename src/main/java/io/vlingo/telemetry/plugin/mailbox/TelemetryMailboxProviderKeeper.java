// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Mailbox;
import io.vlingo.actors.MailboxProvider;
import io.vlingo.actors.MailboxProviderKeeper;
import io.vlingo.telemetry.tracing.TracingBaggage;

public class TelemetryMailboxProviderKeeper implements MailboxProviderKeeper {
  private final MailboxProviderKeeper delegate;
  private final MailboxTelemetry telemetry;
  private final TracingBaggage baggage;

  public TelemetryMailboxProviderKeeper(final MailboxProviderKeeper delegate, final MailboxTelemetry telemetry, final TracingBaggage baggage) {
    this.delegate = delegate;
    this.telemetry = telemetry;
    this.baggage = baggage;
  }

  @Override
  public Mailbox assignMailbox(final String name, final int hashCode) {
    return delegate.assignMailbox(name, hashCode);
  }

  @Override
  public void close() {
    delegate.close();
  }

  @Override
  public String findDefault() {
    return delegate.findDefault();
  }

  @Override
  public void keep(final String name, final boolean isDefault, final MailboxProvider mailboxProvider) {
    delegate.keep(name, isDefault, new TelemetryMailboxProvider(telemetry, mailboxProvider, baggage));
  }

  @Override
  public boolean isValidMailboxName(final String candidateMailboxName) {
    return delegate.isValidMailboxName(candidateMailboxName);
  }
}
