// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.telemetry.plugin.mailbox;

import io.vlingo.ActorsTest;
import io.vlingo.actors.Configuration;
import io.vlingo.actors.Registrar;
import io.vlingo.telemetry.DefaultTelemetryProvider;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MailboxTelemetryPluginTest extends ActorsTest {
  private Registrar registrar;
  private MailboxTelemetryPlugin plugin;

  @Before
  public void setUp() throws Exception {
    super.setUp();

    registrar = mock(Registrar.class);
    plugin = new MailboxTelemetryPlugin();

    world.registerDynamic("telemetry", new DefaultTelemetryProvider().provideFrom(world));
    doReturn(world).when(registrar).world();
    plugin.configuration().build(Configuration.define());
  }

  @Test
  public void testThatRegistersATelemetryMailboxInTheRegistrar() {
    plugin.start(registrar);
    verify(registrar).registerMailboxProviderKeeper(any());
  }
}