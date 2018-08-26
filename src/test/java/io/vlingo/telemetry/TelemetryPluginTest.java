package io.vlingo.telemetry;

import io.vlingo.ActorsTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TelemetryPluginTest extends ActorsTest {
  private TelemetryPlugin plugin;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    plugin = new TelemetryPlugin();
  }

  @Test
  public void testThatPluginLoadsDefaultProvider() {
    plugin.configuration().build(world.configuration());
    plugin.start(world);

    Assert.assertTrue(plugin.telemetry() instanceof MicrometerTelemetry);
  }

  @After
  public void tearDown() throws Exception {
    plugin.close();
    world.terminate();
  }
}
