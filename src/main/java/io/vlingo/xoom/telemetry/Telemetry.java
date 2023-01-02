// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import io.vlingo.xoom.actors.World;

import java.io.Closeable;
import java.util.concurrent.Callable;

public interface Telemetry<UNDERLYING> extends Closeable {
  final class Tag {
    private final String name;
    private final String value;

    private Tag(final String name, final String value) {
      this.name = name;
      this.value = value;
    }

    public static Tag of(final String name, final String value) {
      return new Tag(name, value);
    }

    public final String name() {
      return name;
    }

    public final String value() {
      return value;
    }
  }

  UNDERLYING underlying();

  void count(final String counterName, final Integer actualCount, final Tag... tags);
  void gauge(final String gaugeName, final Integer deltaCount, final Tag... tags);
  <RETURN> RETURN time(final String timerName, final Callable<RETURN> callable, final Tag... tags) throws Exception;

  static Telemetry<?> from(final World world) {
    return world.resolveDynamic("telemetry", Telemetry.class);
  }
}
