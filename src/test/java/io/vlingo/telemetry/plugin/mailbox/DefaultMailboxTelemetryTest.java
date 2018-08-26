// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry.plugin.mailbox;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.vlingo.ActorsTest;
import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Message;
import io.vlingo.actors.NoProtocol;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class DefaultMailboxTelemetryTest extends ActorsTest {
  private MeterRegistry registry;
  private Message message;
  private String addressOfActor;
  private DefaultMailboxTelemetry telemetry;

  @Before
  public void setUp() throws Exception {
    super.setUp();

    addressOfActor = UUID.randomUUID().toString();
    final Actor receiver = testWorld.actorFor(
        Definition.has(RandomActor.class, Definition.NoParameters, addressOfActor),
        NoProtocol.class
    ).actorInside();

    message = mock(Message.class);
    doReturn(receiver).when(message).actor();

    registry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, Clock.SYSTEM);
    telemetry = new DefaultMailboxTelemetry(registry);
  }

  @Test
  public void testThatSendAndReceiveRegistersACounterOnTheActorsMailbox() {
    telemetry.onSendMessage(message);
    assertPendingMessagesNumberIs(1, 1);

    telemetry.onReceiveMessage(message);
    assertPendingMessagesNumberIs(0, 0);
  }

  @Test
  public void testThatReceivingOnAnEmptyMailboxCountsAsIdle() {
    telemetry.onReceiveEmptyMailbox();
    assertIdlesAre(1);
  }

  @Test
  public void testThatFailedSentsAreCounted() {
    telemetry.onSendMessageFailed(message, expectedException());
    assertFailuresAre(1, 1, DefaultMailboxTelemetry.FAILED_SEND);
    assertIllegalStateExceptionCount(1);
  }

  @Test
  public void testThatReceivingFailuresAreCounted() {
    telemetry.onReceiveMessageFailed(expectedException());
    assertIllegalStateExceptionCount(1);
  }

  @Test
  public void testThatDeliveringFailuresAreCountedFromMessage() {
    telemetry.onDeliverMessageFailed(message, expectedException());

    assertFailuresAre(1, 1, DefaultMailboxTelemetry.FAILED_DELIVER);
    assertIllegalStateExceptionCount(1);
  }

  private void assertPendingMessagesNumberIs(final int expectedActor, final int expectedClass) {
    AtomicInteger byActorPending = telemetry.gaugeFor(message, DefaultMailboxTelemetry.SCOPE_INSTANCE, DefaultMailboxTelemetry.PENDING);
    assertEquals(expectedActor, byActorPending.get());

    AtomicInteger byActorClassPending = telemetry.gaugeFor(message, DefaultMailboxTelemetry.SCOPE_CLASS, DefaultMailboxTelemetry.PENDING);
    assertEquals(expectedClass, byActorClassPending.get());
  }

  private void assertFailuresAre(final int expectedActor, final int expectedClass, final String typeOfOp) {
    double failures = telemetry.counterFor(message, DefaultMailboxTelemetry.SCOPE_INSTANCE, typeOfOp + ".IllegalStateException").count();
    assertEquals(expectedActor, failures, 0.0);

    double globalFailuresOfActorClass = telemetry.counterFor(message, DefaultMailboxTelemetry.SCOPE_CLASS, typeOfOp + ".IllegalStateException").count();
    assertEquals(expectedClass, globalFailuresOfActorClass, 0.0);
  }

  private void assertIllegalStateExceptionCount(final int expected) {
    double count = telemetry.counterForException(IllegalStateException.class).count();
    assertEquals(expected, count, 0.0);
  }

  private void assertIdlesAre(final int expectedIdles) {
    double idle = telemetry.idleCounter().count();
    assertEquals(expectedIdles, idle, 0.0);
  }

  private IllegalStateException expectedException() {
    return new IllegalStateException("Expected exception");
  }

  public static class RandomActor extends Actor {
  }
}
