// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry.plugin.mailbox;

import java.util.Properties;

import io.vlingo.xoom.actors.Configuration;
import io.vlingo.xoom.actors.Registrar;
import io.vlingo.xoom.actors.plugin.Plugin;
import io.vlingo.xoom.actors.plugin.PluginConfiguration;
import io.vlingo.xoom.actors.plugin.PluginProperties;
import io.vlingo.xoom.actors.plugin.mailbox.DefaultMailboxProviderKeeper;
import io.vlingo.xoom.telemetry.Telemetry;

public class MailboxTelemetryPlugin implements Plugin {
  public static class MailboxTelemetryPluginConfiguration implements PluginConfiguration {
    private static final String NO_NAME = "_No_Name_";

    @Override
    public void build(final Configuration configuration) {
      buildWith(configuration, new PluginProperties(NO_NAME, new Properties()));
    }

    @Override
    public void buildWith(final Configuration configuration, final PluginProperties properties) {
    }

    @Override
    public String name() {
      return MailboxTelemetryPlugin.class.getSimpleName();
    }
  }

  private final MailboxTelemetryPluginConfiguration configuration;

  public MailboxTelemetryPlugin() {
    this(new MailboxTelemetryPluginConfiguration());
  }

  @Override
  public void close() {

  }

  @Override
  public PluginConfiguration configuration() {
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
    Telemetry<?> telemetry = Telemetry.from(registrar.world());
    registrar.registerMailboxProviderKeeper(new TelemetryMailboxProviderKeeper(new DefaultMailboxProviderKeeper(), new DefaultMailboxTelemetry(telemetry)));
  }

  @Override
  public Plugin with(final PluginConfiguration overrideConfiguration) {
    if (overrideConfiguration == null) {
      return this;
    }
    return new MailboxTelemetryPlugin((MailboxTelemetryPluginConfiguration) overrideConfiguration);
  }

  @Override
  public void __internal_Only_Init(final String name, final Configuration configuration, final Properties properties) {
    // no-op
  }

  private MailboxTelemetryPlugin(final MailboxTelemetryPluginConfiguration configuration) {
    this.configuration = configuration;
  }
}
