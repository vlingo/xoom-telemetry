package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TelemetryMessageTest {
  private Message delegate;
  private MailboxTelemetry telemetry;
  private TelemetryMessage telemetryMessage;

  @Before
  public void setUp() throws Exception {
    delegate = mock(Message.class);
    telemetry = mock(MailboxTelemetry.class);

    telemetryMessage = new TelemetryMessage(delegate, telemetry);
  }

  @Test
  public void testThatActorIsDelegated() {
    final Actor actor = mock(Actor.class);
    doReturn(actor).when(delegate).actor();

    final Actor result = telemetryMessage.actor();

    verify(delegate).actor();
    assertEquals(actor, result);
  }

  @Test
  public void testThatDeliverIsTrackedByTelemetry() {
    telemetryMessage.deliver();

    verify(delegate).deliver();
    verify(telemetry).onReceiveMessage(delegate);
  }

  @Test
  public void testThatDeliverErrorsAreTrackedByTelemetry() {
    final IllegalStateException exception = new IllegalStateException("Expected exception");
    doThrow(exception).when(delegate).deliver();

    telemetryMessage.deliver();

    verify(delegate).deliver();
    verify(telemetry, never()).onReceiveMessage(delegate);
    verify(telemetry).onDeliverMessageFailed(delegate, exception);
  }

  @Test
  public void testThatRepresentationIsDelegated() {
    final String representation = UUID.randomUUID().toString();
    doReturn(representation).when(delegate).representation();

    final String actual = telemetryMessage.representation();

    verify(delegate).representation();
    assertEquals(representation, actual);
  }

  @Test
  public void testThatAIsStowedIsDelegated() {
    final boolean stowed = new Random().nextBoolean();
    doReturn(stowed).when(delegate).isStowed();

    final boolean actual = telemetryMessage.isStowed();

    verify(delegate).isStowed();
    assertEquals(stowed, actual);
  }
}