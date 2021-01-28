package com.github;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.World;

public class Test {
    public interface Ping {
        void ping();
    }

    public static class PingActor extends Actor implements Ping {
        @Override
        public void ping() {
            System.out.println("Ping!");

            stage().actorOf(Pong.class, stage().addressFactory().from("100"))
                    .andFinallyConsume(Pong::pong);
        }
    }

    public interface Pong {
        void pong();
    }

    public static class PongActor extends Actor implements Pong {
        @Override
        public void pong() {
            System.out.println("Pong!");

            stage().actorOf(Foo.class, stage().addressFactory().from("200"))
                    .andFinallyConsume(Foo::foo);
        }
    }

    public interface Foo {
        void foo();
    }

    public static class FooActor extends Actor implements Foo {
        @Override
        public void foo() {
            System.out.println("Foo!");
        }
    }


    public static void main(String[] args) {
        World world = World.start("test");

        Ping ping = world.actorFor(Ping.class, PingActor.class);

        world.stage().actorFor(Pong.class, Definition.has(PongActor.class, Definition.NoParameters), world.addressFactory().from("100"));
        world.stage().actorFor(Foo.class, Definition.has(FooActor.class, Definition.NoParameters), world.addressFactory().from("200"));

        ping.ping();
    }
}
