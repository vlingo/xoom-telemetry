// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import io.vlingo.xoom.actors.World;

@FunctionalInterface
public interface TelemetryProvider<UNDERLYING> {
  Telemetry<UNDERLYING> provideFrom(final World world);

  class InvalidTelemetryProviderException extends Exception {
    private static final long serialVersionUID = 1L;

    InvalidTelemetryProviderException(final String className, final Throwable cause) {
      super(className + " is not a valid TelemetryProvider.", cause);
    }
  }

  @SuppressWarnings("unchecked")
  static <UNDERLYING> TelemetryProvider<UNDERLYING> fromClass(final String className) throws TelemetryProvider.InvalidTelemetryProviderException {
    try {
      Class<? extends TelemetryProvider<UNDERLYING>> providerClass = (Class<? extends TelemetryProvider<UNDERLYING>>) Class.forName(className);
      return providerClass.getDeclaredConstructor().newInstance();
    } catch (final Exception ex) {
      throw new TelemetryProvider.InvalidTelemetryProviderException(className, ex);
    }
  }
}
