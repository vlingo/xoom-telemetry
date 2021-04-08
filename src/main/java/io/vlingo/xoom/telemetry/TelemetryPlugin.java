// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import java.io.IOException;
import java.util.Properties;

import io.vlingo.xoom.actors.Configuration;
import io.vlingo.xoom.actors.Registrar;
import io.vlingo.xoom.actors.plugin.Plugin;
import io.vlingo.xoom.actors.plugin.PluginConfiguration;
import io.vlingo.xoom.actors.plugin.PluginProperties;

public class TelemetryPlugin implements Plugin {
  public static class TelemetryPluginConfiguration implements PluginConfiguration {
    private static final String NO_NAME = "_No_Name_";
    private TelemetryProvider<?> telemetryProvider;

    @Override
    public void build(final Configuration configuration) {
      buildWith(configuration, new PluginProperties(NO_NAME, new Properties()));
    }

    @Override
    public void buildWith(final Configuration configuration, final PluginProperties properties) {
      try {
        this.telemetryProvider = TelemetryProvider.fromClass(properties.getString("providerClass", "io.vlingo.xoom.telemetry.DefaultTelemetryProvider"));
      } catch (TelemetryProvider.InvalidTelemetryProviderException e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public String name() {
      return TelemetryPluginConfiguration.class.getSimpleName();
    }
  }

  private final TelemetryPluginConfiguration configuration;
  private Telemetry<?> telemetry;

  public TelemetryPlugin() {
    this(new TelemetryPluginConfiguration());
  }

  @Override
  public void close() {
    try {
      telemetry.close();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public TelemetryPluginConfiguration configuration() {
    return configuration;
  }

  @Override
  public String name() {
    return configuration.name();
  }

  @Override
  public int pass() {
    return 0;
  }

  @Override
  public void start(final Registrar registrar) {
    telemetry = configuration().telemetryProvider.provideFrom(registrar.world());
    registrar.world().registerDynamic("telemetry", telemetry);
  }

  @Override
  public Plugin with(final PluginConfiguration overrideConfiguration) {
    if (overrideConfiguration == null) {
      return this;
    }
    return new TelemetryPlugin((TelemetryPluginConfiguration) overrideConfiguration);
  }

  @Override
  public void __internal_Only_Init(final String name, final Configuration configuration, final Properties properties) {
    // no-op
  }

  public Telemetry<?> telemetry() {
    return telemetry;
  }

  private TelemetryPlugin(final TelemetryPluginConfiguration configuration) {
    this.configuration = configuration;
  }
}
