#Serenity Project
Serenity BDD is an open source library that aims to make the idea of living documentation a reality.

Here is the [link](https://serenity-bdd.github.io/docs/guide/maven) for simple documentation.

###Steps to Create Project
1.Create a maven project called `SerenityProject`

2.under `pom.xml`
1.add below property section
```xml 
<properties>
<maven.compiler.source>11</maven.compiler.source>
<maven.compiler.target>11</maven.compiler.target>
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```
3.Add dependencies
```xml
        <!-- This is for base support for anything we do with serenity-->
            <dependency>
                <groupId>net.serenity-bdd</groupId>
                <artifactId>serenity-core</artifactId>
                <version>2.4.4</version>
            </dependency>
        <!-- This is the dependency that wrap up rest assured with additional serenity support-->
            <dependency>
                <groupId>net.serenity-bdd</groupId>
                <artifactId>serenity-rest-assured</artifactId>
                <version>2.4.4</version>
            </dependency>

            
            <dependency>
                <groupId>org.junit.jupiter</groupId>
                <artifactId>junit-jupiter</artifactId>
                <version>5.8.2</version>
            </dependency>
                    <!--Junit 5 support for serenity -->
            <dependency>
                <groupId>io.github.fabianlinz</groupId>
                <artifactId>serenity-junit5</artifactId>
                <version>1.6.0</version>
            </dependency>

```

4.Add Build plugins
```xml
  <build>
        <plugins>
          
            <!--            This is where the report is being generated after the test run -->
            <plugin>
                <groupId>net.serenity-bdd.maven.plugins</groupId>
                <artifactId>serenity-maven-plugin</artifactId>
                <version>2.4.4</version>
                <executions>
                    <execution>
                        <id>serenity-reports</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>aggregate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <!--         We want to run all the tests then generate one report -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M5</version>
                <configuration>
                    <testFailureIgnore>true</testFailureIgnore>
                </configuration>
            </plugin>
        </plugins>
    </build>
```
5.Create a package called `com/cydeo` under `src/test/java`
1. under cydeo create `spartan` and under spartan create `admin` packages

2. create a class called `SpartanAdminGetTest`

6.Create regular @Test rest api class `getAllSpartan` and send a request.

7.This is just regular test, in order to make it recognized by serenity reports
* add annotation class level : `@SerenityTest`
* it is coming from `import net.serenitybdd.junit5.SerenityTest;
  `

8. Add a properties file with exact name `serenity.properties`
   right under project level
* add following lines to properties file
```properties
serenity.project.name=API Report
serenity.test.root=cydeo
```
9.In order to generate serenity report, we need to use maven goal
* if you are using command line: `mvn clean verify`  cmd+enter or ctrl+enter if you dont have maven installed locally
* if you are using IntelliJ buttons;
    * first click on clean then click on verify
* your report will be generated under target as HTML Report

10.This is for serenity to pick up log and eliminate the warning
```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>1.7.30</version>
</dependency>
```
11. We were able to generate test report, however there are no details about the request and response.
    In order to see the details then we need to use the `given() when() then()` methods coming from Serenity.
    * Instead of importing rest assured `given` import,use below
        * `import static net.serenitybdd.rest.SerenityRest.given;`
    * From this point on, all details will be picked up by Serenity report, you will see **Rest Query** button on the individual tests

12. The way that assert the response and show it as a steps in Serenity report is using `Ensure` class (from `import net.serenitybdd.rest.Ensure;`)
```java
        Ensure.that("Status code is 200",validatableResponse -> validatableResponse.statusCode(201) );

        Ensure.that("Content-type is JSON",vRes -> vRes.contentType(ContentType.JSON));

        Ensure.that("Id is 15", vRes -> vRes.body("id",is(15)));

```

# Serenity Day 2

We did Parameterized Test using Junit 5 @ParameterizedTest using Value Source and CSVSource.

Here is the [full example](src/test/java/cydeo/spartan/editor/SpartanEditorPostTest.java)


## Serenity Properties

There are two special file that used by serenity for properties.

Both of them can be accessible using the [ConfigReader Class](src/test/java/utilities/ConfigReader.java) by providing property name.


### `serenity.properties`
This file can either be under
- root directory
- `src/test/resources`

It's a key-value pair like regular properties file only difference is ,
all key value pairs will be added as system env variable at run time

### `serenity.conf`
The structure looks as below , we can store environment specific data
and run the project against different environment by selecting environment in maven command
```
environments {
    default {
        base.url = "http://library1.cydeo.com"
        librarian.username = "librarian60@library"
        librarian.password = "NyLIiSAm"
    }
    library1 {
        base.url = "http://library1.cydeo.com"
        librarian.username = "librarian60@library"
        librarian.password = "NyLIiSAm"
    }
    library2 {
        base.url = "http://library2.cydeo.com"
        librarian.username = "librarian60@library"
        librarian.password = "Lj5VU4Tq"
    }
    library3 {
        base.url = "http://library3.cydeo.com"
        librarian.username = "librarian60@library"
        librarian.password = "yNFS9ghh"
    }
    all{
        base.path = "/rest/v1"
    }
}
```
It starts with environments :
- `default` : for default values if no envronment specified
- custom environment name like library1 library2
- key value pair under that environment
- common value for all environment goes to `all` section


--- 
Selecting Environment can be achieved as below using maven command
```
mvn clean verify -Denvironment=YourEnvironmentNameGoesHere
```

Actual code we wrote to access these key value pair is using `getProperty` method of `ConfigReader` class
```java
        System.out.println( ConfigReader.getProperty("base.url")  );
        System.out.println( ConfigReader.getProperty("librarian.username")  );
```
Above code will generate different result according to the environment you selected.

This will enable running same set of tests against different environment. 

How to run specific class with maven
```
mvn clean verify -Denvironment=YourEnvironmentNameGoesHere -Dtest=ClassName 
```



# How to Get Report for Serenity after Jenkins Test Execution

1. Manage Jenkins --> Manage Plugins --> Available

2. Install ```Tuchydides Test Reports```  plugin and Restart Jenkins

3. Create new Jenkins Job for Serenity Project

4. Build--> Invoke Top Level Maven Targets -->``` mvn clean verify ```

5. Post Build Action --> ``` Publish Tuchydides Test Reports ```

6. Report path --> ``` target/site/serenity ```

7. Build Jenkins Job

8. Click over Jenkins Job

9. Click ```Tuchydides Test Reports ``` to see ```Serenity Report```

