package io.vlingo.telemetry;

import io.vlingo.actors.Configuration;
import io.vlingo.actors.Registrar;
import io.vlingo.actors.plugin.Plugin;
import io.vlingo.actors.plugin.PluginConfiguration;
import io.vlingo.actors.plugin.PluginProperties;

import java.io.IOException;
import java.util.Properties;

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
        this.telemetryProvider = TelemetryProvider.fromClass(properties.getString("providerClass", "io.vlingo.telemetry.DefaultTelemetryProvider"));
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
    this.configuration = new TelemetryPluginConfiguration();
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
    // registrar.world().register("telemetry", telemetry);
  }

  public Telemetry<?> telemetry() {
    return telemetry;
  }
}
