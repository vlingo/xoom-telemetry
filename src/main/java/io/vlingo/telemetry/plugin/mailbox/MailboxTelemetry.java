// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Message;

public interface MailboxTelemetry {
  void onSendMessage(final Message message);
  void onSendMessageFailed(final Message message, final Throwable exception);

  void onReceiveEmptyMailbox();
  void onReceiveMessage(final Message message);
  void onReceiveMessageFailed(final Throwable exception);
  void onDeliverMessageFailed(final Message message, final Throwable exception);
}
