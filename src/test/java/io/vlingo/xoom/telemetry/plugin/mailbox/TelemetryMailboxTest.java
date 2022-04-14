// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry.plugin.mailbox;

import io.vlingo.xoom.actors.Mailbox;
import io.vlingo.xoom.actors.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TelemetryMailboxTest {
  private MailboxTelemetry telemetry;
  private Mailbox delegate;
  private Mailbox telemetryMailbox;
  private Message message;

  @Before
  public void setUp() {
    telemetry = mock(MailboxTelemetry.class);
    delegate = mock(Mailbox.class);
    message = mock(Message.class);

    telemetryMailbox = new TelemetryMailbox(telemetry, delegate);
  }

  @Test
  public void testThatSendingAMessageIsTracked() {
    telemetryMailbox.send(message);

    verify(delegate).send(any());
    verify(telemetry).onSendMessage(any());
  }

  @Test
  public void testThatFailingSendAMessageIsTracked() {
    final Throwable expectedException = new IllegalStateException("Expected exception ocurred");
    doThrow(expectedException).when(delegate).send(any());

    telemetryMailbox.send(message);

    verify(delegate).send(any());
    verify(telemetry).onSendMessageFailed(any(), eq(expectedException));
    verify(telemetry, never()).onSendMessage(any());
  }

  @Test
  public void testThatPullingAnEmptyMailboxIsTracked() {
    doReturn(null).when(delegate).receive();

    Message nullMessage = telemetryMailbox.receive();

    verify(delegate).receive();
    verify(telemetry).onReceiveEmptyMailbox();
    verify(telemetry, never()).onReceiveMessage(null);

    assertNull(nullMessage);
  }

  @Test
  public void testThatPullingANotEmptyMailboxIsTracked() {
    doReturn(message).when(delegate).receive();

    Message notNullMessage = telemetryMailbox.receive();

    verify(delegate).receive();
    verify(telemetry).onReceiveMessage(message.actor());
    verify(telemetry, never()).onReceiveEmptyMailbox();

    assertEquals(message, notNullMessage);
  }

  @Test(expected = IllegalStateException.class)
  public void testThatFailingPullingAMailboxIsTracked() {
    final Throwable expectedException = new IllegalStateException("Expected exception ocurred");
    doThrow(expectedException).when(delegate).receive();

    try {
      telemetryMailbox.receive();
    } finally {
      verify(delegate).receive();
      verify(telemetry).onReceiveMessageFailed(expectedException);
      verify(telemetry, never()).onReceiveMessage(any());
      verify(telemetry, never()).onReceiveEmptyMailbox();
    }
  }

  @Test
  public void testThatCloseIsDelegated() {
    telemetryMailbox.close();
    verify(delegate).close();
  }

  @Test
  public void testThatIsClosedIsDelegated() {
    boolean closed = new Random().nextBoolean();

    doReturn(closed).when(delegate).isClosed();
    boolean result = telemetryMailbox.isClosed();

    verify(delegate).isClosed();
    assertEquals(closed, result);
  }

  @Test
  public void testThatIsDeliveringIsDelegated() {
    boolean delivering = new Random().nextBoolean();

    doReturn(delivering).when(delegate).isDelivering();
    boolean result = telemetryMailbox.isDelivering();

    verify(delegate).isDelivering();
    assertEquals(delivering, result);
  }

  @Test
  public void testThatRunIsDelegated() {
    telemetryMailbox.run();
    verify(delegate).run();
  }
}
