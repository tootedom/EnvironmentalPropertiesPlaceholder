**Table of Contents**  *generated with [DocToc](http://doctoc.herokuapp.com/)*

- [How to obtain the library](#how-to-obtain-the-library)
- [Overview](#overview)
- [Changing the defaults](#changing-the-defaults)
	- [Changing the name of the default properties file](#changing-the-name-of-the-default-properties-file)
	- [Changing the extension of the properties file](#changing-the-extension-of-the-properties-file)
	- [Changing the location that configuration is sourced](#changing-the-location-that-configuration-is-sourced)
	- [Changing the location that environmental overrides are sourced](#changing-the-location-that-environmental-overrides-are-sourced)
- [Multi Environmental Variable Configuration](#multi-environmental-variable-configuration)
- [Operational Overrides](#operational-overrides)
- [Thread Safety](#thread-safety)
- [Map<String,String>](#map<stringstring>)
- [Property Merging Strictness](#property-merging-strictness)
- [Property values and Placeholder (${}) Replacement](#property-values-and-placeholder-${}-replacement)
	- [Obtaining Resolved and Unresolved properties one at a time](#obtaining-resolved-and-unresolved-properties-one-at-a-time)
	- [Using the environment to resolve placeholders](#using-the-environment-to-resolve-placeholders)
	- [Trimming Property Values of whitespace](#trimming-property-values-of-whitespace)
	- [Thread Safety](#thread-safety)
- [Composite Builder](#composite-builder)
- [Using in Spring](#using-in-spring)

## How to obtain the library


```xml
    <dependency>
       <groupId>org.greencheek</groupId>
       <artifactId>environment-properties-merger-core</artifactId>
       <version>1.0.0</version>
    </dependency>
```

The maven repositories are located at:

```
   https://raw.github.com/tootedom/tootedom-mvn-repo/master/releases/
   https://raw.github.com/tootedom/tootedom-mvn-repo/master/snapshots/
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

The above can be achieved with the following:

    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
    p = mergerBuilder.buildProperties();

The idea of the library is that you distribute along with your application configuration properties.  The **default.properties**
contains a set of properties that contain defaults for the application configuration, lets say something like:

    └── config
        ├── default.properties

contains:

```
   database.url=jdbc:mysql://localhost/admin
   database.username=user
   database.password=pass
```

You then provide overrides to the defaults within properties files that match the value of the ${ENV} variable as it
exists on your platform's architecture.  For example on your production environment you provide:

   └── config
        └── environments
            └── production.properties

that contains configuration specific to the live environment

```
   database.url=jdbc:mysql://bernard-app.dbw.production/admin
   database.username=user
   database.password=pass
```

This way you can have varying configuration for the application that is distributed along with your application.


***
## Changing the defaults

By Default the library reads configuration from the classpath, looking for *config/default.properties* and then files that
are override based on the value of the *${ENV}* variable from *config/environments/${ENV}.properties*.  This defaults are
changable:

### Changing the name of the default properties file

The following will change the name of the default properties file that is sourced from *default.properties* to *global.properties*:

      PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
      .setNameOfDefaultPropertiesFile("global");
      p = mergerBuilder.buildProperties();

### Changing the extension of the properties file

The following will change the extension of the properties file from *properties* to *props*:

      PropertiesMergerBuilder mergerBuilder4 = new EnvironmentSpecificPropertiesMergerBuilder()
      .setNameOfDefaultPropertiesFile("global")
      .setExtensionForPropertiesFile("props");
      p = mergerBuilder4.buildProperties();

The separator character can be changed from "." to something else, i.e. "-" via the following:

      .setExtensionSeparatorCharForPropertiesFile('-')

### Changing the location that configuration is sourced

By default the configuration files is sourced from the classpath location: *config/*.  This can be changed to either read
from a different location on the classpath, or can be change to read from the file system:

* Classpath

Changes to source from */app/config/* on the classpath

    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
    .setLocationForLoadingConfigurationProperties("classpath:/app/config");
    Properties p = mergerBuilder.buildProperties();

* FileSystem

Changes to source configuration from /data/opsoverrides/myapp/config

    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
    .setLocationForLoadingConfigurationProperties("file:/data/application/config");
    Properties p = mergerBuilder.buildProperties();


Changing to read from C:/data/opsoverrides/myapp/config on windows

```java
    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
    .setLocationForLoadingConfigurationProperties("file:C:/data/opsoverrides/myapp/config");
    Properties p = mergerBuilder.buildProperties();
```

> It is often helpful for operations teams, or fellow developers not to deploy configuration properties embedded within
> .jar files, but have then distributed to a filesystem location that is on the classpath, i.e WEB-INF/classess in a web
> application.  It just helps when troubleshooting issues, and the verification of configuration quickly (over having to
> extract the configuration from a jar - say from within WEB-INF/lib.  However, there's no strict rule about this; just
> a preference.  Often when distributing a library for use by multiple applications, it is not avoidable; and configuration
> needs to be distributed with the jar.  (See Operational Overrides later on).


### Changing the location that environmental overrides are sourced

By default the environmental overrides are sourced from the **environment** directory within the location
specified for where configuration is to be sourced.  For example the follow defines the application configuration
to be read from the classpath at **/data/config**, and the environmental configuration is sourced from **/data/config/envs**:

```java
    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
    .setLocationForLoadingConfigurationProperties("classpath:/data/config")
    .setRelativeLocationOfFilesOverridingDefaultProperties("envs");
    Properties p = mergerBuilder.buildProperties();
```


***
## Multi Environmental Variable Configuration

The library goes one step futher and allows you to configure the environmental setting so that you can differ not
just on one environmental or system property, but on a sequence of environmental variables.  For example, imagine
you had both the environmental variable ${ENV} and system property ${os.arch}.  You can configure library to merge configuration in
order fashion such that it sources:

- /config/default.properties
- /config/environments/production.properties
- /config/environments/production.x86_64.properties

```java
    PropertiesMergerBuilder resolverEnvAndOsBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
    .setVariablesUsedForSwitchingConfiguration(new String[] {"ENV","ENV,os.arch"});
```

Running the above on a Macbook pro (OSX Snow Leopard/Lion), with the **ENV** environment variable set to **production**
would resulting the following files being sourced and merged.

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
in /data/opsoverrides/*<appname>*/config  (the value of **appname** you need to specify.  On a windows machine the default
is c:\data\opsoverrides\*<appname>*\config).  For example the following will create a PropertiesMerger that reads
operational overrides from the directory */data/opsoverrides/bernard/config* (on windows this will be C:)

```java
    PropertiesMergerBuilder resolverEnvAndOsBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
    .setVariablesUsedForSwitchingConfiguration(new String[] {"ENV","ENV,os.arch"})
    .setApplicationName("bernard")
    Properties p = mergerBuilder.buildProperties();
```

Given the above, and a value of **production** for the *${ENV}* variable, the configuration the will be sourced in the
order top to bottom:

    classpath:
    |
    └── config
        ├── default.properties
        └── environments
            └── production.properties
    filesystem:
    |
    └── /data
        ├── opsoverrides
            ├── bernard
                ├── config
                    ├── default.properties
                        └── environments
                                    └── production.properties

In otherwords, the files are source in the order:

* classpath:/config/default.properties
* classpath:/config/environments/${ENV}.properties
* filesystem:/data/opsoverrides/bernard/config/default.properties
* filesystem:/data/opsoverrides/bernard/config/environments/${ENV}.properties

The location of where the *Operational Overrides* are loaded from can be changed, via setting a property on
the MergerBuilder.  For example, the following defines the operational overrides location to be
**/data/ops/applicationX/config**:

```java
    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
    .setVariablesUsedForSwitchingConfiguration(new String[] {"ENV","ENV,os.arch"})
    .setLocationForLoadingOperationalOverrides("file:/data/ops/applicationX/config");
    Properties p = mergerBuilder.buildProperties();
```

The above in essence will result in the following being sourced:

* classpath:/config/default.properties
* classpath:/config/environments/${ENV}.properties
* filesystem:/data/ops/applicationX/config/default.properties
* filesystem:/data/ops/applicationX/config/environments/${ENV}.properties

>
> Please note it is possible to set the operational overrides to point to a classpath location,
> by changing the **file:** prefix to **classpath:** on the resource.  However, the idea behind operational overrides
> is to give the ability to operational teams to adjust application properties quickly and easily.
> therefore a **file:** location is probably the preferrable option.

## Thread Safety

The PropertiesMergerBuilder is not thread safe, it is intended to by used by a single thread in order to create a PropertiesMerger
instance.  The PropertiesMerger instance is then thread safe for use by multiple threads.  The Builder is responsible for
creating the PropertiesMerger, once the PropertiesMerger has been created; it is safe for use by multiple threads.  The
Properties object that is returned by the following java call, is thread safe:

```java
   PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
   Properties p = mergerBuilder.buildProperties();
```

The above is equivalent to:

```java
   PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
   PropertiesMerger merger = mergerBuilder.build();
   Properties p = merger.getMergedProperties();
```

## Obtaining a Map instead of a Properties Object

Rather than a Properties object you can obtain a Map<String,String> of the properties:

```java
   PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
   PropertiesMerger merger = mergerBuilder.build();
   Map<String,String> map = merger.getMergedPropertiesAsMap();
```

## Property Merging Strictness

It is when the PropertyMerger is constructed; that the properties files are read from the classpath and/or filesystem.
During the resolution process (merging), the merger will compare the properties in the original Properties file (default.properties)
and compare them against the properties in the overriding file.  If a property is defined in the overriding file; that
did not exist in the default properties; a warning will be logged (Just letting you know that a new property exists; for
which there is no default value).  For example, a spelling mistake exists in the *prod.properties*:

   **default.properties**
   product-inventory.url=http://localhost:9090/api/list

   **prod.properties**
   product-inevntory.url=http://products.live.xxx:9090/api/list

For
```java
   PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
   PropertiesMerger merger = mergerBuilder.build();
```

In testing you would see:

>
>  11:40:24.431 [main] WARN  o.g.u.e.p.m.EnvironmentSpecificPropertiesMerger - NoMatchingPropertyWarning: Property "product-inevntory.url" from overriding properties does not exist in original properties
>

If you would rather have the PropertiesMerger b *strict* and throw and exception and fail to be contructed, you can
set the merger to be strict, as follows:

```java
   PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
   .setStrictMergingOfProperties(true);
   PropertiesMerger merger = mergerBuilder.build();
```

Then, during the construction of the PropertiesMerger from the mergerBuilder's build() method, you would receive the runtime exception:
org.greencheek.utils.environment.propertyplaceholder.resolver.exception.NoMatchingPropertyException:

>
> Exception in thread "main" org.greencheek.utils.environment.propertyplaceholder.resolver.exception.NoMatchingPropertyException: NoMatchingPropertyWarning: Property "product-inevntory.url" from overriding properties does not exist in original properties
>

***
## Property values and Placeholder (${}) Replacement

All of the above configuration/code examples will not replace any variables (placeholder) that are in
the values for the properties in the returned Properties object.  For example, given the following property
contained within **config/default.properties**, lets say,

    database.server.cname=bernard-app.dbw.production
    database.url=jdbc:mysql://${database.server.cname}/admin

The value of **database.url** when obtained from the Properties object will still contain **${database.server.cname}**

```java
    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
    Properties p = mergerBuilder.buildProperties();
    //
    // Results in:
    // {database.url=jdbc:mysql://${database.server.cname}/admin, database.server.cname=bernard-app.dbw.production}
    //
```

In order to have placeholder replacement take place you have to use an additional builder that creates a **PropertiesResolver**.
The previous builder (PropertiesMergerBuilder), creates just a **PropertiesMerger** that is responsible for "*merging*"
the varying properties files that differ based on environmental settings.  The **PropertiesResolver** takes the merged
properties from the **PropertiesMerger** and resolves the variables from the values of the properties

```java
    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
    PropertiesResolverBuilder resolverBuilder = new EnvironmentSpecificPropertiesResolverBuilder();
    PropertiesMerger merger = mergerBuilder.build();
    Properties p = resolverBuilder.buildProperties(merger);
```

Or to reduce the amount of code, a little, you can pass the **PropertiesMergerBuilder** directly to the *buildProperties*
method of the PropertiesResolverBuilder.  The PropertiesResolverBuilder, just calls **build** on the builder, to obtain the
PropertiesMerger from which it obtains the merged properties:

```java
    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
    PropertiesResolverBuilder resolverBuilder = new EnvironmentSpecificPropertiesResolverBuilder();
    Properties p = resolverBuilder.buildProperties(mergerBuilder);
```

### Obtaining Resolved and Unresolved properties one at a time

Everytime you call *resolverBuilder.buildProperties(mergerBuilder);* to obtain a Properties object, a new PropertiesResolver
and a Properties object will be created.  Therefore, you should really only create the Properties object once, and
use it in multiple place.  However, if you have a central place (object) to query that will return both a property with
resolved variable and with unresolved variables; you can create a PropertiesResolver object, and query it for properties.
This will query its internal Properties object it obtained from the merger:

* Create the PropertiesResolver

```java
    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
    PropertiesResolverBuilder resolverBuilder = new EnvironmentSpecificPropertiesResolverBuilder();
    PropertiesMerger merger = mergerBuilder.build();
    PropertiesResolver resolver = resolverBuilder.build(merger);
```

* Query for Resolved Property

```java
   resolver.getProperty("database.url")
   //
   // Returns: jdbc:mysql://bernard-app.dbw.production/admin
   //
```

* Query for UnResolved Property

```java
   resolver.getUnResolvedProperty("database.url")
   //
   // Returns: jdbc:mysql://${database.server.cname}/admin
   //
```


### Using the environment to resolve placeholders

By default the Resolver will also resolve variables (placeholders), within the property values from both
environment varibles available to the java process, and any java system properties (*-D*) that are set.  If you do not
wish for this behaviour then you can turn it off by creating a **VariablePlaceholderValueResolver** and passing it to
the **PropertiesResolverBuilder** via the *setPropertyValueResolver* method:

```java
   PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
   PropertiesResolverBuilder resolverBuilder = new EnvironmentSpecificPropertiesResolverBuilder();

   ValueResolverConfig valueResolver = new VariablePlaceholderValueResolverConfig()
   .setEnvironmentPropertiesResolutionEnabled(false)
   .setSystemPropertiesResolutionEnabled(false);

   resolverBuilder.setPropertyValueResolver(new VariablePlaceholderValueResolver(valueResolver));
   p = resolverBuilder.buildProperties(mergerBuilder);
```

### Trimming Property Values of whitespace

By default the property values returned from the *PropertiesResolver* are trimmed of whitespace; from the beginning
and end of the property value (java.lang.String.trim()).  This can be turned off at the PropertiesResolverBuilder level:

```java
   PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
   PropertiesResolverBuilder resolverBuilder = new EnvironmentSpecificPropertiesResolverBuilder()
   .setTrimmingPropertyValues(false);
   PropertiesResolver resolver = resolverBuilder.build(mergerBuilder.build());
```


### Thread Safety

Like that of the PropertiesMergerBuilder, the **PropertiesResolverBuilder** is not thread safe, it is intended to by
used by a single thread in order to construct a **PropertiesResolver**, which is then safe to use across multiple threads.
This too goes for the **ValueResolver**, and it's configuration.  The **ValueResolver** (VariablePlaceholderValueResolver)
 is thread safe after construction.

Like that of the PropertiesMergerBuilder, you obtain the PropertiesResolver from the builder:

```java
    PropertiesMergerBuilder mergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder();
    PropertiesResolverBuilder resolverBuilder = new EnvironmentSpecificPropertiesResolverBuilder();
    PropertiesMerger merger = mergerBuilder.build();
    PropertiesResolver resolver = resolverBuilder.build(merger);
```

You can then use the constructed PropertiesResolver in multiple threads.  The Properties obtain returned via a
**resolverBuilder.buildProperties(mergerBuilder);** is safe to use across multiple threads too.

***
## Composite Builder

A Composite Builder is avialable that is a combination of the PropertiesMergerBuilder and the PropertiesResolverBuilder
that adds the method **Properties buildResolvedProperties();**, that returns the set of properties, with all
embedded property values resolved.  The properties themselves come from a set of combined property files that are
source dependent on system or environment properties defined by the merger. Any property value that contain variables
(placeholders i.e. ${..})  are resolved.    The composite builder was created as a means to reduce the number of lines
of xml/code when generating a *Properties* object to use as a Spring 3.1 [PropertySources](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/core/env/PropertySource.html)
object.

```xml
    <bean id="environmentalProperties" class="org.springframework.core.env.PropertiesPropertySource">
        <constructor-arg value="myEnvironmentProperties"/>
        <constructor-arg>
            <bean factory-bean="propertiesResolver" factory-method="buildResolvedProperties"/>
        </constructor-arg>
    </bean>

    <bean id="propertiesResolver" class="org.greencheek.utils.environment.propertyplaceholder.resolver.builder.CompositeResolvedPropertiesBuilder">
        <constructor-arg>
            <bean class="org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver">
                <constructor-arg value="false"/> <!-- do not resolve env vars -->
                <constructor-arg value="false"/> <!-- do not resolve system properties -->
            </bean>
        </constructor-arg>
        <property name="locationForLoadingConfigurationProperties" value="classpath:/"/>
        <property name="locationForLoadingOperationalOverrides" value="classpath:/opsoverrides"/>
        <property name="relativeLocationOfFilesOverridingDefaultProperties" value="environments"/>
    </bean>
```

***
## Using in Spring

Prior to PropertySource introduction in Spring 3 and the associate @Profile(value="DEV"), and autowired
org.springframework.core.env.Environment.  The way to define a PropertyPlaceholderConfigurer was as follows:


```xml
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="properties">
            <bean factory-bean="propertiesResolver" factory-method="buildResolvedProperties"/>
        </property>
    </bean>

    <bean id="propertiesResolver" class="org.greencheek.utils.environment.propertyplaceholder.resolver.builder.CompositeResolvedPropertiesBuilder">
        <constructor-arg>
            <bean class="org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver">
                <constructor-arg value="false"/> <!-- resolve env vars -->
                <constructor-arg value="false"/> <!-- resolve system properties -->
            </bean>
        </constructor-arg>
        <property name="locationForLoadingConfigurationProperties" value="classpath:/"/>
        <property name="locationForLoadingOperationalOverrides" value="classpath:/opsoverrides"/>
        <property name="relativeLocationOfFilesOverridingDefaultProperties" value="environments"/>
    </bean>
```

This allowed you to register a Property Placeholder that you resolve ${} properties within the application contexts,
and @Value annotations:

```java
    @Value("${message}")
	private String message;
```

With the introduction of Spring 3.1.1, there has been a move to the use of PropertySource implementations and the
use of the autowired Environment in order to obtain properties:

```java
    @Autowired
	private Environment env;

    @RequestMapping(value="/headers/{noofheaders}",method=RequestMethod.GET)
    public String returnHeaders(final @PathVariable("noofheaders") int noheaders,
    						    Model model,HttpServletRequest request)
    {
        String s = env.getProperty("message");
    }
```

The PropertiesMerger works within this environment, and properties can be made available through the Environment
object via the use of a custom [ApplicationContextInitializer](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/context/ApplicationContextInitializer.html)
that is then set on either the DispatcherServlet or the ContextLoaderListener

* DispatcherServlet:

```xml
    <servlet>
		<servlet-name>servlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
		<init-param>
			<param-name>contextInitializerClasses</param-name>
        	<param-value>org.greencheek.playground.web.spring.PropertiesMergerApplicationContextInitializer</param-value>
        </init-param>
	</servlet>
```

* ContextLoaderListener:

```xml
    <context-param>
        <param-name>contextInitializerClasses</param-name>
        <param-value>org.greencheek.playground.web.spring.PropertiesMergerApplicationContextInitializer</param-value>
    </context-param>

    <listener>
    	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
```

You would then make a PropertySource that is instance (PropertiesPropertySource)[http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/core/env/PropertiesPropertySource.html]
populated from the properties obtain from either the merger or the resolver:

```java
  public class PropertiesMergerApplicationContextInitializer
         implements ApplicationContextInitializer<ConfigurableApplicationContext>  {

	 private static final Properties environmentalProperties;
	 static {
        PropertiesMerger merger = new EnvironmentSpecificPropertiesMergerBuilder().build();
        environmentalProperties = merger.getMergedProperties();
        // Or have the following for the resolved ${} properties:
        // environmentalProperties = new EnvironmentSpecificPropertiesResolverBuilder().buildProperties(merger);

	 }

	 public void initialize(ConfigurableApplicationContext applicationContext) {
		 initialise(applicationContext);
	 }

	 public static void initialise(ConfigurableApplicationContext applicationContext) {
	     // Add the merged properties to the applicationContext environment
	     // So that the properties are available in the @Autowired Environment object
		 applicationContext.getEnvironment()
		 .getPropertySources()
		 .addFirst(new PropertiesPropertySource("p",environmentalProperties));
	 }
  }
```

Please be aware the above **DOES NOT** register a PropertySourcesPlaceholderConfigurer as a BeanFactory Post Processor
so the properties will not be made available to @Value annotations, unless you register a PropertySourcesPlaceholderConfigurer
yourself:

```xml
    <bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
           <property name="properties">
           		<bean factory-bean="propertiesResolver" factory-method="buildResolvedProperties"/>
           </property>
    </bean>

    <bean id="propertiesResolver" class="org.greencheek.utils.environment.propertyplaceholder.resolver.builder.CompositeResolvedPropertiesBuilder">
            <constructor-arg>
                <bean class="org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver">
                    <constructor-arg value="false"/> <!-- resolve env vars -->
                    <constructor-arg value="false"/> <!-- resolve system properties -->
                </bean>
            </constructor-arg>
            <property name="locationForLoadingConfigurationProperties" value="classpath:/"/>
            <property name="locationForLoadingOperationalOverrides" value="classpath:/opsoverrides"/>
            <property name="relativeLocationOfFilesOverridingDefaultProperties" value="environments"/>
    </bean>
```

You could also declare java based @Configuration, and create the **PropertySourcesPlaceholderConfigurer** as an @Bean.
If the PropertySourcesPlaceholderConfigurer is only used for resolving properties for the Web Application Components,
you could add @Configuration to the PropertiesMergerApplicationContextInitializer above, for example (a PropertySourcesPlaceholderConfigurer
has to be registered in each separate context, i.e. the configurer isn't inherited by the WebApplicationContext from the root ApplicationContext
like that of other beans, it only affects the context in which it is registered):

```java
    import java.util.Properties;

    import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesMergerBuilder;
    import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesResolverBuilder;
    import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
    import org.springframework.context.ApplicationContextInitializer;
    import org.springframework.context.ConfigurableApplicationContext;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
    import org.springframework.core.env.MutablePropertySources;
    import org.springframework.core.env.PropertiesPropertySource;

    @Configuration
    public class AppConfig implements
    		ApplicationContextInitializer<ConfigurableApplicationContext> {

    	private static final Properties environmentalProperties;
    	static {
    		PropertiesMerger merger = new EnvironmentSpecificPropertiesMergerBuilder().build();
    		environmentalProperties = new EnvironmentSpecificPropertiesResolverBuilder().buildProperties(merger);
    	}

    	public void initialize(ConfigurableApplicationContext applicationContext) {
    		initialise(applicationContext);
    	}

    	public static void initialise(ConfigurableApplicationContext applicationContext) {
    		applicationContext
    				.getEnvironment()
    				.getPropertySources()
    				.addFirst(new PropertiesPropertySource("p",environmentalProperties));
    	}

    	@Bean
    	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    		PropertySourcesPlaceholderConfigurer config = new PropertySourcesPlaceholderConfigurer();

    		MutablePropertySources sources = new MutablePropertySources();
    		sources.addFirst(new PropertiesPropertySource("p",environmentalProperties));

    		config.setPropertySources(sources);

    		return config;
    	}

    }
```

