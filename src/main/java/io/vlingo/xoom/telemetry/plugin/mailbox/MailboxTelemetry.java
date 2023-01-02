// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry.plugin.mailbox;

import io.vlingo.xoom.actors.Actor;

public interface MailboxTelemetry {
  void onSendMessage(final Actor actor);
  void onSendMessageFailed(final Actor actor, final Throwable exception);

  void onReceiveEmptyMailbox();
  void onReceiveMessage(final Actor actor);
  void onReceiveMessageFailed(final Throwable exception);
  void onDeliverMessageFailed(final Actor actor, final Throwable exception);
}
