# UPPAAL Meta-Model [![Java CI with Maven](https://github.com/uppaal-emf/uppaal-meta-model/actions/workflows/maven.yml/badge.svg)](https://github.com/uppaal-emf/uppaal-meta-model/actions/workflows/maven.yml)
An Eclipse EMF based Meta-Model for networks of timed automata as they appear in UPPAAL (https://uppaal.org/).
Intended to be used as a library for other model based tooling.
Depends on the Eclipse Modeling Framework and -Libraries. Build using Eclipse Tycho.

## HowTo Build
This project requires the development kit for JavaSE 17 or higher *and* JavaSE 11 to build.
Make sure you have both of them installed and correctly set up your `toolchains.xml` file
to point to their install directory:
```
<?xml version="1.0" encoding="UTF-8"?>
<toolchains xmlns="http://maven.apache.org/TOOLCHAINS/1.1.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/TOOLCHAINS/1.1.0 http://maven.apache.org/xsd/toolchains-1.1.0.xsd">
	<toolchain>
		<type>jdk</type>
		<provides>
			<id>JavaSE-11</id>
			<version>11</version>
		</provides>
		<configuration>
			<jdkHome>/path/to/jdk11</jdkHome>
		</configuration>
	</toolchain>
	<toolchain>
		<type>jdk</type>
		<provides>
			<id>JavaSE-17</id>
			<version>17</version>
		</provides>
		<configuration>
			<jdkHome>/path/to/jdk17</jdkHome>
		</configuration>
	</toolchain>
</toolchains>
```

The project then can be build from the root directory by executing `./mvnw compile` on Linux and
MacOS and by `mvnw.cmd compile` on Windows respectively.

To retrieve installable packages execute the `package` maven goal.
Binary and source bundles are then aggregated under `org.muml.uppaal.package/target`.

Note that code generation from the `org.muml.uppaal/model/uppaal.genmodel` Generator Model
is part of the build process and therefore generated sources are *not* checked in.
Code Generation is conducted during the built via a Modeling Workflow described in
`org.muml.uppaal/src/org/muml/uppaal/GenerateModel.mw2`. 
