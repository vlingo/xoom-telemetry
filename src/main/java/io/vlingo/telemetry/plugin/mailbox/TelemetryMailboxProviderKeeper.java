// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Mailbox;
import io.vlingo.actors.MailboxProvider;
import io.vlingo.actors.MailboxProviderKeeper;

public class TelemetryMailboxProviderKeeper implements MailboxProviderKeeper {
  private final MailboxProviderKeeper delegate;
  private final MailboxTelemetry telemetry;

  public TelemetryMailboxProviderKeeper(final MailboxProviderKeeper delegate, final MailboxTelemetry telemetry) {
    this.delegate = delegate;
    this.telemetry = telemetry;
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
    delegate.keep(name, isDefault, new TelemetryMailboxProvider(telemetry, mailboxProvider));
  }

  @Override
  public boolean isValidMailboxName(final String candidateMailboxName) {
    return delegate.isValidMailboxName(candidateMailboxName);
  }
}
