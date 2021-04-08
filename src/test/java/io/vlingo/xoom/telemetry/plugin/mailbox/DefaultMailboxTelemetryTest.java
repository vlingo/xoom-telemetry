// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry.plugin.mailbox;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.vlingo.xoom.ActorsTest;
import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Message;
import io.vlingo.xoom.actors.NoProtocol;
import io.vlingo.xoom.telemetry.MicrometerTelemetry;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.vlingo.xoom.telemetry.plugin.mailbox.DefaultMailboxTelemetry.IDLE;
import static io.vlingo.xoom.telemetry.plugin.mailbox.DefaultMailboxTelemetry.PREFIX;
import static java.util.Collections.singletonList;
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
            NoProtocol.class,
        Definition.has(RandomActor.class, Definition.NoParameters, addressOfActor)
    ).actorInside();

    message = mock(Message.class);
    doReturn(receiver).when(message).actor();

    registry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, Clock.SYSTEM);
    telemetry = new DefaultMailboxTelemetry(new MicrometerTelemetry(registry));
  }

  @Test
  public void testThatSendAndReceiveRegistersACounterOnTheActorsMailbox() {
    telemetry.onSendMessage(message.actor());
    assertPendingMessagesNumberIs(1);

    telemetry.onReceiveMessage(message.actor());
    assertPendingMessagesNumberIs(0);
  }

  @Test
  public void testThatReceivingOnAnEmptyMailboxCountsAsIdle() {
    telemetry.onReceiveEmptyMailbox();
    assertIdlesAre(1);
  }

  @Test
  public void testThatFailedSentsAreCounted() {
    telemetry.onSendMessageFailed(message.actor(), expectedException());
    assertFailuresAre(1, DefaultMailboxTelemetry.FAILED_SEND);
    assertIllegalStateExceptionCount(1);
  }

  @Test
  public void testThatReceivingFailuresAreCounted() {
    telemetry.onReceiveMessageFailed(expectedException());
    assertIllegalStateExceptionCount(1);
  }

  @Test
  public void testThatDeliveringFailuresAreCountedFromMessage() {
    telemetry.onDeliverMessageFailed(message.actor(), expectedException());

    assertFailuresAre(1, DefaultMailboxTelemetry.FAILED_DELIVER);
    assertIllegalStateExceptionCount(1);
  }

  private void assertPendingMessagesNumberIs(final int expectedActor) {
    Gauge byActorPending = registry.get(PREFIX + "RandomActor.pending").tags(singletonList(Tag.of("Address", addressOfActor))).gauge();
    assertEquals(expectedActor, byActorPending.value(), 0);
  }

  private void assertFailuresAre(final int expectedActor, final String typeOfOp) {
    double failures = registry.get(PREFIX + "RandomActor." + typeOfOp + ".IllegalStateException").tags(singletonList(Tag.of("Address", addressOfActor))).counter().count();
    assertEquals(expectedActor, failures, 0.0);
  }

  private void assertIllegalStateExceptionCount(final int expected) {
    double count = registry.get(PREFIX + "IllegalStateException").counter().count();
    assertEquals(expected, count, 0.0);
  }

  private void assertIdlesAre(final int expectedIdles) {
    double idle = registry.get(PREFIX + IDLE).counter().count();
    assertEquals(expectedIdles, idle, 0.0);
  }

  private IllegalStateException expectedException() {
    return new IllegalStateException("Expected exception");
  }

  public static class RandomActor extends Actor {
  }
}
