# UPPAAL Meta-Model [![Java CI with Maven](https://github.com/uppaal-emf/uppaal-meta-model/actions/workflows/build.yml/badge.svg)](https://github.com/uppaal-emf/uppaal-meta-model/actions/workflows/build.yml)
An Eclipse EMF based Meta-Model for networks of timed automata as they appear in UPPAAL (https://uppaal.org/).
Intended to be used as a library for other model based tooling.
Depends on the Eclipse Modeling Framework and -Libraries. Built using Eclipse Tycho.

## HowTo Build
This project requires the JavaSE 17 development kit for compilation *and* JavaSE 11 to perform dependency resolution via tycho (since it is the Runtime Version of the PlugIns).

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

You can build from the root directory by executing `./mvnw compile` on Linux and
MacOS and by `mvnw.cmd compile` on Windows respectively.

Note that code generation from the `org.muml.uppaal/model/uppaal.genmodel` Generator Model
is part of the build process and therefore generated sources are *not* checked in.
Code Generation is conducted during the built via a Modelling Workflow described in
`org.muml.uppaal/src/org/muml/uppaal/GenerateModel.mw2`. 

## Packaging
To retrieve installable packages execute the `package` maven goal.
Binary and source bundles are aggregated under `org.muml.uppaal.package/target`
(See the descriptors under `org.muml.uppaal.package/src/assembly/`).

The `package` goal also tries to resolve all `p2` dependencies to real maven ones
and strip all build-related configuration from the `pom.xml` files.
The following plugin configurations take care of this:
```
<plugin>
	<groupId>org.eclipse.tycho</groupId>
	<artifactId>tycho-packaging-plugin</artifactId>
	<version>${tycho-version}</version>
	<executions>
		<execution>
			<id>prepare-tycho-pom</id>
			<phase>prepare-package</phase>
			<goals>
				<goal>update-consumer-pom</goal>
			</goals>
			<configuration>
				<deleteOnExit>true</deleteOnExit>
				<includeP2Dependencies>true</includeP2Dependencies>
				<mapP2Dependencies>true</mapP2Dependencies>
			</configuration>
		</execution>
	</executions>
</plugin>
<plugin>
	<groupId>org.codehaus.mojo</groupId>
	<artifactId>flatten-maven-plugin</artifactId>
	<version>1.3.0</version>
	<executions>
		<execution>
			<id>flatten-pom</id>
			<phase>prepare-package</phase>
			<goals>
				<goal>flatten</goal>
			</goals>
			<configuration>
				<flattenMode>defaults</flattenMode>
				<flattenDependencyMode>direct</flattenDependencyMode>
			</configuration>
		</execution>
		<execution>
			<id>clean-flattened-pom</id>
			<phase>clean</phase>
			<goals>
				<goal>clean</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```

## CI Configuration
There are two CI workflow files under `.github/workflows/`:
* `build.yml` runs the maven `package` goal whenever a commit is pushed to main or a pull request to main.
The build is run under Windows, Ubuntu and MacOS, after which all artifacts from the Ubuntu Build are uploaded.
* `release.yml` runs whenever a release is published on github. It then performs a release build under Ubuntu and deploys all artifacts to github packages.

### CI Friendly Versions
This project uses mavens CI friendly versioning scheme. This means that the version qualifier defaults to `-SNAPSHOT`, but can be overridden by `-Dqualifier=.<qualifier> -DforceContextQualifier=<qualifier>` maven args to build a release.
In the release CI, `<qualifier>` is replaced with the datetime format `YYmmddHHmm` of the current commit (See https://tycho.eclipseprojects.io/doc/latest/tycho-packaging-plugin/build-qualifier-mojo.html and https://maven.apache.org/maven-ci-friendly.html).
