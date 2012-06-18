Environmental Properties Merger.
---------------------------------

* auto-gen TOC:
{:toc}
```
   Not Yet Available in Maven Repo
```

```xml
    <dependency>
       <groupId>org.greencheek</groupId>
       <artifactId>environment-properties-merger-core</artifactId>
       <version>1.0.0-SNAPSHOT</version>
    </dependency>
```

## Overview

A simple library that allows for the sourcing of a standard java .properties file
(http://docs.oracle.com/javase/6/docs/api/java/util/Properties.html) for use in application configuration.

The properties files are sourced from either the classpath (classpath:) or filesystem (filesystem:) and can be overridden
based on either environment variables or system properties.  For example.  Imagine in your architecture, you have defined
the environment variable "${ENV}", which denotes the environment in which the current server resides.  This
variable has a different value for the different environments you have, for example:

- ci
- integration
- test
- loadtest
- staging
- production

The library by default allows you to have the following structure of files within you project (src/main/resources)

    └── config
        ├── default.properties
        └── environments
            └── ci.properties
            └── integration.properties
            └── test.properties
            └── loadtest.properties
            └── staging.properties
            └── production.properties

At run time, given the value of "production" for the ${ENV} variable, the lib will return a Properties object that is
a combination (merge) of:

    └── config
        ├── default.properties
        └── environments
            └── production.properties

What this gives you is the ability to have different configuration deployed along with your application that has
varying configuration based on environmental settings.

***
## Multi Environmental Variable Configuration

The library goes one step futher and allows you to configure the environmental setting so that you can differ not
just on one environmental or system property, but on a sequence of environmental variables.  For example, imagine
you had both the environmental variable ${ENV} and system property ${os.arch}.  You can configure library to merge configuration in
order fashion such that it sources:

- /config/default.properties
- /config/environments/production.properties
- /config/environments/production.x86_64.properties


    resolverEnvAndOsBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
    resolverEnvAndOsBuilder.setVariablesUsedForSwitchingConfiguration(new String[] {"ENV","ENV,os.arch"});
    └── config
        ├── default.properties
        └── environments
            └── production.properties
            └── production.x86_64.properties

***
## Operational Overrides

The library also has the concept of "*Operational Overrides*".  This gives the operations department a location on the
file system in which they can overwrite a specific property value used by your application without having to modify the
configuration that you distribute with your application.  This can be useful for properties such as passwords, or
database connection strings etc; that only the operations department might know about.

By default the library will look for these overrides within the directory (on the filesystem),
in /data/opsoverrides/*<appname>*/config  (the value of *appname* you need to specify.  On a windows machine the default
is c:\data\opsoverrides\*<appname>*\config).

The location of where the *Operational Overrides* are lo


