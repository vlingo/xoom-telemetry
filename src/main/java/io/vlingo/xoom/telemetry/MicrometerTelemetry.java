// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MicrometerTelemetry implements Telemetry<MeterRegistry> {
  private final MeterRegistry meterRegistry;
  private final Map<Integer, AtomicInteger> gauges;

  public MicrometerTelemetry(final MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
    this.gauges = new LinkedHashMap<>();
  }

  @Override
  public MeterRegistry underlying() {
    return meterRegistry;
  }

  @Override
  public void count(final String counterName, final Integer actualCount, final Tag... tags) {
    counterOrNew(counterName, toMicrometerTag(tags)).increment(actualCount);
  }

  @Override
  public void gauge(final String gaugeName, final Integer deltaCount, final Tag... tags) {
    gaugeOrNew(gaugeName, toMicrometerTag(tags)).addAndGet(deltaCount);
  }

  @Override
  public <RETURN> RETURN time(final String timerName, final Callable<RETURN> callable, final Tag... tags) throws Exception {
    return timerOrNew(timerName, toMicrometerTag(tags)).recordCallable(callable);
  }

  @Override
  public void close() throws IOException {
    meterRegistry.close();
  }

  private Counter counterOrNew(final String counterName, final Iterable<io.micrometer.core.instrument.Tag> tags) {
    try {
      return meterRegistry.get(counterName).tags(tags).counter();
    } catch (Exception e) {
      return meterRegistry.counter(counterName, tags);
    }
  }

  private AtomicInteger gaugeOrNew(final String counterName, final Iterable<io.micrometer.core.instrument.Tag> tags) {
    final int hashOfGauge = hashOf(counterName, tags);
    AtomicInteger gaugeValue = gauges.get(hashOfGauge);

    if (gaugeValue == null) {
      gaugeValue = meterRegistry.gauge(counterName, tags, new AtomicInteger(0));
      gauges.put(hashOfGauge, gaugeValue);
    }

    return gaugeValue;
  }

  private Timer timerOrNew(final String timerName, final Iterable<io.micrometer.core.instrument.Tag> tags) {
    try {
      return meterRegistry.get(timerName).tags(tags).timer();
    } catch (Exception ex) {
      return meterRegistry.timer(timerName, tags);
    }
  }

  private Iterable<io.micrometer.core.instrument.Tag> toMicrometerTag(final Tag... vlingoTags) {
    return Arrays.stream(vlingoTags).map(vtag ->
        new ImmutableTag(vtag.name(), vtag.value())
    ).collect(Collectors.toList());
  }

  private int hashOf(final String counterName, final Iterable<io.micrometer.core.instrument.Tag> tags) {
    StringBuilder builder = new StringBuilder(counterName);
    tags.forEach(tag -> builder.append(tag.getKey()).append(tag.getValue()));

    return builder.toString().hashCode();
  }
}
