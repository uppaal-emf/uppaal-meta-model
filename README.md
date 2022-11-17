# UPPAAL Meta-Model [![Java CI with Maven](https://github.com/uppaal-emf/uppaal-meta-model/actions/workflows/maven.yml/badge.svg)](https://github.com/uppaal-emf/uppaal-meta-model/actions/workflows/maven.yml)

## HowTo Build
This project requires the development kit for JavaSE 17 or higher *and* JavaSE 11 to build.
Make sure you have both of them installed and correctly set up your `toolchains.xml` file
to point to their install directory:
```
<?xml version="1.0" encoding="UTF8"?>
<toolchains xmlns="http://maven.apache.org/TOOLCHAINS/1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/TOOLCHAINS/1.1.0 http://maven.apache.org/xsd/toolchains-1.1.0.xsd">
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

The project then can be build by executing `./mvnw compile` on Linux and MacOS and by `mvnw.cmd compile` on Windows respectively.
