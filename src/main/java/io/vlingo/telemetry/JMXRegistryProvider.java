// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.vlingo.actors.World;

public class JMXRegistryProvider implements RegistryProvider {
  @Override
  public MeterRegistry provide(final World world) {
    return new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);
  }
}
