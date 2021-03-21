# vlingo-telemetry

[![Javadocs](http://javadoc.io/badge/io.vlingo/vlingo-telemetry.svg?color=brightgreen)](http://javadoc.io/doc/io.vlingo/vlingo-telemetry) [![Build](https://github.com/vlingo/vlingo-telemetry/workflows/Build/badge.svg)](https://github.com/vlingo/vlingo-telemetry/actions?query=workflow%3ABuild) [ ![Download](https://api.bintray.com/packages/vlingo/vlingo-platform-java/vlingo-telemetry/images/download.svg) ](https://bintray.com/vlingo/vlingo-platform-java/vlingo-telemetry/_latestVersion) [![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/vlingo-platform-java/community)

The VLINGO XOOM platform SDK Reactive metrics collector plugin supporting XOOM ACTORS, XOOM HTTP, XOOM LATTICE, XOOM STREAMS, and others.

Docs: https://docs.vlingo.io/vlingo-telemetry

### Important
If using snapshot builds [follow these instructions](https://github.com/vlingo/vlingo-platform#snapshots-repository) or you will experience failures.

### Bintray

```xml
  <repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
  </repositories>
  <dependencies>
    <dependency>
      <groupId>io.vlingo</groupId>
      <artifactId>vlingo-telemetry</artifactId>
      <version>1.5.0</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
```

```gradle
dependencies {
    compile 'io.vlingo:vlingo-telemetry:1.5.0'
}

repositories {
    jcenter()
}
```

License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2020 VLINGO LABS. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.
