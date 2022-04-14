// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.telemetry;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.TestUntil;
import io.vlingo.xoom.actors.testkit.TestWorld;
import org.junit.After;
import org.junit.Before;

public abstract class ActorsTest {
  protected World world;
  protected TestWorld testWorld;

  public TestUntil until;

  protected ActorsTest() {
  }

  public TestUntil until(final int times) {
    return TestUntil.happenings(times);
  }

  @Before
  public void setUp() throws Exception {
    testWorld = TestWorld.start("test");
    world = testWorld.world();
  }

  @After
  public void tearDown() throws Exception {
    testWorld.terminate();
  }
}
