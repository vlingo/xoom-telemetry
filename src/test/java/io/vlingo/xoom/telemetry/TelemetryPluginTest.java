// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import io.vlingo.xoom.telemetry.ActorsTest;
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
