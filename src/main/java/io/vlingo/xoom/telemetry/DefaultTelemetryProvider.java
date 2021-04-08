// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.vlingo.xoom.actors.World;

public class DefaultTelemetryProvider implements TelemetryProvider<MeterRegistry> {
  @Override
  public Telemetry<MeterRegistry> provideFrom(final World world) {
    return new MicrometerTelemetry(new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM));
  }
}
