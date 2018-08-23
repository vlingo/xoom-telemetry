package io.vlingo.telemetry;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.vlingo.actors.World;

public class DefaultTelemetryProvider implements TelemetryProvider<MeterRegistry> {
  @Override
  public Telemetry<MeterRegistry> provideFrom(final World world) {
    return new MicrometerTelemetry(new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM));
  }
}
