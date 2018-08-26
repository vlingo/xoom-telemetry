// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry;

import io.micrometer.core.instrument.MeterRegistry;
import io.vlingo.actors.World;

@FunctionalInterface
public interface RegistryProvider {
  MeterRegistry provide(final World world);

  class InvalidRegistryProviderException extends Exception {
    InvalidRegistryProviderException(String className, Throwable ex) {
      super(className + " is not a valid RegistryProvider.", ex);
    }
  }

  static RegistryProvider fromClass(final String className) throws InvalidRegistryProviderException {

    try {
      Class<? extends RegistryProvider> providerClass = (Class<? extends RegistryProvider>) Class.forName(className);
      return providerClass.getDeclaredConstructor().newInstance();
    } catch (final Exception ex) {
      throw new InvalidRegistryProviderException(className, ex);
    }
  }
}
