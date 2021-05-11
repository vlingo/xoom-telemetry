package io.vlingo.xoom.telemetry.implnative;

import io.micrometer.core.instrument.Clock;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.telemetry.DefaultTelemetryProvider;
import io.vlingo.xoom.telemetry.MicrometerTelemetry;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

public final class NativeImpl {
  @CEntryPoint(name = "Java_io_vlingo_xoom_telemetrynative_Native_start")
  public static int start(@CEntryPoint.IsolateThreadContext long isolateId, CCharPointer name) {
    final String nameString = CTypeConversion.toJavaString(name);
    World world = World.startWithDefaults(nameString);
    new MicrometerTelemetry(new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM));
    new DefaultTelemetryProvider().provideFrom(world);
    return 0;
  }
}
