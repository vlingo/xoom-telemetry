package io.vlingo.telemetry;

import io.vlingo.actors.World;

@FunctionalInterface
public interface TelemetryProvider<UNDERLYING> {
  Telemetry<UNDERLYING> provideFrom(final World world);

  class InvalidTelemetryProviderException extends Exception {
    InvalidTelemetryProviderException(String className, Throwable ex) {
      super(className + " is not a valid TelemetryProvider.", ex);
    }
  }

  static <UNDERLYING> Telemetry<UNDERLYING> fromClass(final String className) throws TelemetryProvider.InvalidTelemetryProviderException {

    try {
      Class<? extends Telemetry<UNDERLYING>> providerClass = (Class<? extends Telemetry<UNDERLYING>>) Class.forName(className);
      return providerClass.getDeclaredConstructor().newInstance();
    } catch (final Exception ex) {
      throw new TelemetryProvider.InvalidTelemetryProviderException(className, ex);
    }
  }
}
