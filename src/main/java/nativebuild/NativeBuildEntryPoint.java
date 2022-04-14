// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package nativebuild;

import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

import io.micrometer.core.instrument.Clock;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.telemetry.DefaultTelemetryProvider;
import io.vlingo.xoom.telemetry.MicrometerTelemetry;

public final class NativeBuildEntryPoint {
  @SuppressWarnings("resource")
  @CEntryPoint(name = "Java_io_vlingo_xoom_telemetrynative_Native_start")
  public static int start(@CEntryPoint.IsolateThreadContext long isolateId, CCharPointer name) {
    final String nameString = CTypeConversion.toJavaString(name);
    World world = World.startWithDefaults(nameString);
    new MicrometerTelemetry(new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM));
    new DefaultTelemetryProvider().provideFrom(world);
    return 0;
  }
}
