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
