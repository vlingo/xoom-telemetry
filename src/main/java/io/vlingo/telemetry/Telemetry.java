package io.vlingo.telemetry;

import java.io.Closeable;
import java.util.concurrent.Callable;

public interface Telemetry<UNDERLYING> extends Closeable  {
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
}
