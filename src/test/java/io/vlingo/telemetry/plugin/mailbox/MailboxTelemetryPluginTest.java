package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.actors.Configuration;
import io.vlingo.actors.Registrar;
import io.vlingo.actors.plugin.PluginProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MailboxTelemetryPluginTest {
  private Registrar registrar;
  private MailboxTelemetryPlugin plugin;

  @Before
  public void setUp() {
    registrar = mock(Registrar.class);
    plugin = new MailboxTelemetryPlugin();

    plugin.configuration().build(Configuration.define());
  }

  @Test
  public void testThatRegistersATelemetryMailboxInTheRegistrar() {
    plugin.start(registrar);
    verify(registrar).registerMailboxProviderKeeper(any());
  }
}