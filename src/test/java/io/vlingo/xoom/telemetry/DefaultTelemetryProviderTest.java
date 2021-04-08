// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import io.micrometer.jmx.JmxMeterRegistry;
import io.vlingo.xoom.ActorsTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DefaultTelemetryProviderTest extends ActorsTest  {
  private TelemetryProvider<?> defaultTelemetryProvider;

  @Before
  public void setUp() throws Exception {
    super.setUp();

    defaultTelemetryProvider = new DefaultTelemetryProvider();
  }

  @Test
  public void testThatProvidesAMicrometerRegistry() {
    Telemetry<?> telemetry = defaultTelemetryProvider.provideFrom(world);

    assertTrue(telemetry instanceof MicrometerTelemetry);
    assertTrue(telemetry.underlying() instanceof JmxMeterRegistry);
  }

  @Test
  public void testThatLoadsFromClass() throws TelemetryProvider.InvalidTelemetryProviderException {
    defaultTelemetryProvider = TelemetryProvider.<MicrometerTelemetry>fromClass("io.vlingo.xoom.telemetry.DefaultTelemetryProvider");
    assertTrue(defaultTelemetryProvider instanceof DefaultTelemetryProvider);
  }

  @Test(expected = TelemetryProvider.InvalidTelemetryProviderException.class)
  public void testThatFailsWhenClassDoesNotExist() throws TelemetryProvider.InvalidTelemetryProviderException {
    TelemetryProvider.<MicrometerTelemetry>fromClass("io.vlingo.xoom.telemetry.parrot.random.ParrotDefaultTelemetryProvider");
  }
}