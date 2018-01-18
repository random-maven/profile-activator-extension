
### Profile Activator Extension

Provide flexible Maven profile activation via script.

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/mojohaus/versions-maven-plugin.svg?label=License)](http://www.apache.org/licenses/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.carrotgarden.maven/profile-activator-extension/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.carrotgarden.maven/profile-activator-extension)
[![Bintray Download](https://api.bintray.com/packages/random-maven/maven/profile-activator-extension/images/download.svg) ](https://bintray.com/random-maven/maven/profile-activator-extension/_latestVersion)
[![Travis Status](https://travis-ci.org/random-maven/profile-activator-extension.svg?branch=master)](https://travis-ci.org/random-maven/profile-activator-extension/builds)

Similar extensions
* [kpiwko/el-profile-activator-extension](https://github.com/kpiwko/el-profile-activator-extension)
* [rrialq/jsr223-profile-activator-extension](https://github.com/rrialq/jsr223-profile-activator-extension)

Extension features
* comprehensive [logging](https://github.com/random-maven/profile-activator-extension/blob/master/note/activator-logging.md)
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
${project}/.mvn/extensions.xml
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
   isdef property1 && isdef property2 && property1 == property2 
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

Activator script has a merged view of
* `project` properties: `pom.xml/<properties>`
* `system` properties form: `System.getProperties()`
* `user` properties from user-provided command line `-D` options

Variable scopes
* variables are injected in the default script variable scope;
  note that some scripting engines have no default scope
* there is also an extension-provided variable container `script`,
  which contains a copy of the default scope,
  use it to access variables with script-invalid characters in the name
  or with engines which have no default scope

Example `script` variable container access syntax for a given script engine flavor:
the following expression extracts the value of ```user.name``` system property:
* Groovy syntax: `script["user.name"]` 
* JavaScript syntax: `script["user.name"]` 
* MVEL Script syntax: `script["user.name"]` 

  
### Build yourself

```
cd /tmp
git clone git@github.com:random-maven/profile-activator-extension.git
cd profile-activator-extension
./mvnw.sh clean install -B -P skip-test
```
