
### Profile Activator Extension

Provide flexible Maven profile activation via script.

Reference feature implementation, [vote for MNG-6345](https://issues.apache.org/jira/browse/MNG-6345).

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/mojohaus/versions-maven-plugin.svg?label=License)](http://www.apache.org/licenses/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.carrotgarden.maven/profile-activator-extension/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.carrotgarden.maven/profile-activator-extension)
[![Bintray Download](https://api.bintray.com/packages/random-maven/maven/profile-activator-extension/images/download.svg) ](https://bintray.com/random-maven/maven/profile-activator-extension/_latestVersion)
[![Travis Status](https://travis-ci.org/random-maven/profile-activator-extension.svg?branch=master)](https://travis-ci.org/random-maven/profile-activator-extension/builds)

Similar extensions
* [kpiwko/el-profile-activator-extension](https://github.com/kpiwko/el-profile-activator-extension)
* [rrialq/jsr223-profile-activator-extension](https://github.com/rrialq/jsr223-profile-activator-extension)

Extension features
* comprehensive [logging](https://github.com/random-maven/profile-activator-extension/blob/master/note/activator-logging.md)
* exposes resolved [project pom.xml](https://maven.apache.org/pom.html)
* works with Maven 3.5
([activation via AND](https://issues.apache.org/jira/browse/MNG-4565))
* scripting with 
[Groovy](https://en.wikipedia.org/wiki/Groovy_(programming_language)), 
[JavaScript](https://en.wikipedia.org/wiki/JavaScript), 
[MVEL Script](https://en.wikipedia.org/wiki/MVEL)


### Usage Examples:

Profile activator is configured in two steps: 

1. Register [project extension](https://github.com/random-maven/profile-activator-extension/blob/master/.mvn/extensions.xml)
```
${project.basedir}/.mvn/extensions.xml
```
```xml
<extension>
   <groupId>com.carrotgarden.maven</groupId>
   <artifactId>profile-activator-extension</artifactId>
</extension>
```

2. Provide activation script inside the [magic property](https://github.com/random-maven/profile-activator-extension/blob/master/src/main/java/com/carrotgarden/maven/activator/Activator.java)
```xml
<profile>
   <activation>
      <property>
         <name>[ACTIVATOR:MVELSCRIPT]</name>
         <value>
<![CDATA[
   isdef property1 && isdef property2 && property1 == "hello-maven" && project.packaging == "bundle" 
]]>
         </value>
       </property>
   </activation>
</profile>
```   
* example activator in [Groovy](https://github.com/random-maven/profile-activator-extension/tree/master/src/it/test-groovyscript/pom.xml)
* example activator in [JavaScript](https://github.com/random-maven/profile-activator-extension/tree/master/src/it/test-javascript/pom.xml)
* example activator in [MVEL Script](https://github.com/random-maven/profile-activator-extension/tree/master/src/it/test-mvelscript/pom.xml)

### Script variables

Tip: check [examples first](https://github.com/random-maven/profile-activator-extension/tree/master/src/it).

Activator script has access to
* merged properties, as a map of type 
  [`java.util.Map<String, String>`](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
  named `value`
* resolved project model, as an object of type
  [`org.apache.maven.model.Model`](http://maven.apache.org/ref/3.5.2/maven-model/apidocs/org/apache/maven/model/Model.html)
  named `project`

#### Merged Properties 

Activator script has ordered and merged view of
* `project` properties: `pom.xml/<properties>`
* `system` properties form: `System.getProperties()`
* `user` properties from user-provided command line `-D` options

Merged properties scopes
* properties are injected in the default script variable scope
  (for scripting engines that have such)
* there is also an extension-provided map `value`,
  which contains a copy of the default scope
* use `value` map to access variables
  with dot in the name or when there is no default scope

Example default scope access syntax:
the following expression extracts the value of
```pom.xml/<properties>/<property1>``` 
* Groovy syntax: not available, use `value["property1"]` 
* JavaScript syntax: `property1` (same result as `value["property1"]`)
* MVEL Script syntax: `property1` (same result as `value["property1"]`) 

Example `value` map access syntax:
the following expression extracts the value of ```user.name``` 
which originally comes from a system property,
and which is considered "invalid name" in the default scope:
* Groovy syntax: `value["user.name"]` 
* JavaScript syntax: `value["user.name"]` 
* MVEL Script syntax: `value["user.name"]` 

#### Resolved Project Model 

Resolved interpolated [project descriptor](https://maven.apache.org/pom.html)
is exposed
in the form of [project model bean](http://maven.apache.org/ref/3.5.2/maven-model/apidocs/org/apache/maven/model/Model.html)
.

Activator script has access to the project model as an object named `project`.

Example `project` object access syntax:
the following expression extracts the value of ```pom.xml/<packaging>``` 
which is available as [`public String getPackaging()`](http://maven.apache.org/ref/3.5.2/maven-model/apidocs/org/apache/maven/model/Model.html#getPackaging--)
* Groovy syntax: `project.packaging` (same result as `project.getPackaging()`)
* JavaScript syntax: `project.packaging` (same result as `project.getPackaging()`) 
* MVEL Script syntax: `project.packaging` (same result as `project.getPackaging()`)

#### Variable existence check

Normally, scripting engine will throw an error
when trying to access a variable which is not defined. 

Example variable existence check syntax:
* Groovy syntax: not available, use null-test: `if ( value["property1"] ) { ... }`
* JavaScript syntax: `if ( typeof property1 !== 'undefined' )  { ... }`
* MVEL Script syntax: `if ( isdef property1 ) { ... }`

In practice, `null-test` is sufficient in most cases
* `if ( value["property1"] ) { ... }`

Project [model members](http://maven.apache.org/ref/3.5.2/maven-model/apidocs/org/apache/maven/model/Model.html)
are always defined, but can return `null`. 

### Build yourself

```
cd /tmp
git clone git@github.com:random-maven/profile-activator-extension.git
cd profile-activator-extension
./mvnw.sh clean install -B -P skip-test
```
