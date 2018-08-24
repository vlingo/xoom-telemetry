package io.vlingo.telemetry;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.jmx.JmxMeterRegistry;
import io.vlingo.ActorsTest;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestWorld;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class DefaultTelemetryProviderTest extends ActorsTest  {
  private TelemetryProvider defaultTelemetryProvider;

  @Before
  public void setUp() throws Exception {
    super.setUp();

    defaultTelemetryProvider = new DefaultTelemetryProvider();
  }

  @Test
  public void testThatProvidesAMicrometerRegistry() {
    Telemetry telemetry = defaultTelemetryProvider.provideFrom(world);

    assertTrue(telemetry instanceof MicrometerTelemetry);
    assertTrue(telemetry.underlying() instanceof JmxMeterRegistry);
  }

  @Test
  public void testThatLoadsFromClass() throws TelemetryProvider.InvalidTelemetryProviderException {
    defaultTelemetryProvider = TelemetryProvider.<MicrometerTelemetry>fromClass("io.vlingo.telemetry.DefaultTelemetryProvider");
    assertTrue(defaultTelemetryProvider instanceof DefaultTelemetryProvider);
  }

  @Test(expected = TelemetryProvider.InvalidTelemetryProviderException.class)
  public void testThatFailsWhenClassDoesNotExist() throws TelemetryProvider.InvalidTelemetryProviderException {
    TelemetryProvider.<MicrometerTelemetry>fromClass("io.vlingo.telemetry.parrot.random.ParrotDefaultTelemetryProvider");
  }
}