// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;

public class MicrometerTelemetryTest {
  private static final String RANDOM_NAME = UUID.randomUUID().toString();
  private static final String RANDOM_TAG = UUID.randomUUID().toString();
  private static final String RANDOM_TAG_VALUE = UUID.randomUUID().toString();
  private static final int RANDOM_COUNT = 10;

  private MeterRegistry meterRegistry;
  private Telemetry<MeterRegistry> telemetry;

  @Before
  public void setUp() throws Exception {
    meterRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);
    telemetry = new MicrometerTelemetry(meterRegistry);
  }

  @Test
  public void testThatUnderlyingReturnsTheConcreteImplementation() {
    assertEquals(meterRegistry, telemetry.underlying());
  }

  @Test
  public void testThatCountDelegatesToTheMeterRegistry() {
    telemetry.count(RANDOM_NAME, RANDOM_COUNT, Telemetry.Tag.of(RANDOM_TAG, RANDOM_TAG_VALUE));
    Counter counter = meterRegistry.get(RANDOM_NAME).tag(RANDOM_TAG, RANDOM_TAG_VALUE).counter();

    assertEquals(RANDOM_COUNT, counter.count(), 0);
  }

  @Test
  public void testThatGaugeDelegatesToTheMeterRegistry() {
    telemetry.gauge(RANDOM_NAME, -RANDOM_COUNT, Telemetry.Tag.of(RANDOM_TAG, RANDOM_TAG_VALUE));
    Gauge gauge = meterRegistry.get(RANDOM_NAME).tag(RANDOM_TAG, RANDOM_TAG_VALUE).gauge();

    assertEquals(-RANDOM_COUNT, gauge.value(), 0);
  }

  @Test
  public void testThatTimingDelegatesToTheMeterRegistry() throws Exception {
    int result = telemetry.time(RANDOM_NAME, () -> {
      Thread.sleep(RANDOM_COUNT);
      return RANDOM_COUNT;
    }, Telemetry.Tag.of(RANDOM_TAG, RANDOM_TAG_VALUE));

    Timer timer = meterRegistry.get(RANDOM_NAME).tag(RANDOM_TAG, RANDOM_TAG_VALUE).timer();

    assertTrue(result >= RANDOM_COUNT);
    assertTrue(timer.totalTime(TimeUnit.MILLISECONDS) >= RANDOM_COUNT);
  }
}
