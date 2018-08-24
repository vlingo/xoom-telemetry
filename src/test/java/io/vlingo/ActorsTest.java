package io.vlingo;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.actors.testkit.TestWorld;
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
