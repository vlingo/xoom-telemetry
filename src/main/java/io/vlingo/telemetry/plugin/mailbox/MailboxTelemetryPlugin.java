package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Configuration;
import io.vlingo.actors.Registrar;
import io.vlingo.actors.plugin.Plugin;
import io.vlingo.actors.plugin.PluginConfiguration;
import io.vlingo.actors.plugin.PluginProperties;
import io.vlingo.actors.plugin.mailbox.DefaultMailboxProviderKeeper;
import io.vlingo.telemetry.Telemetry;

import java.util.Properties;

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
    this.configuration = new MailboxTelemetryPluginConfiguration();
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
}
