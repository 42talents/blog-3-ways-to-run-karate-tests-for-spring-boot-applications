# Example project with Spring-Boot and Karate

[![Java CI with Maven](https://github.com/42talents/spring-boot-karate-example/actions/workflows/maven.yml/badge.svg)](https://github.com/42talents/spring-boot-karate-example/actions/workflows/maven.yml)


## 3 Ways to run Karate Tests for Spring-Boot Applications.

![](alex-knight-2EJCSULRwC8-unsplash.jpg)
<sub>
Photo by <a href="https://unsplash.com/@agk42?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Alex Knight</a> on <a href="https://unsplash.com/s/photos/automation?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>
</sub>

### What is the Karate Framework?
Karate's slogan is **Test Automation Made Simple**. It is a test framework based on Cucumber to write web application tests quickly. You can use it as an alternative to REST-assured API testing.

### Karate Setup within a Spring-Boot Project
Karate comes as a test dependency. In this example, we use maven.
```xml
<dependency>
    <groupId>com.intuit.karate</groupId>
    <artifactId>karate-junit5</artifactId>
    <version>1.2.0</version>
    <scope>test</scope>
</dependency>
```

### Example Karate Test
You write Karate tests in Gherkin. We use a BDD style here. The following example shows how to verify that the spring-boot health actuator resource shows "up".
```gherkin
Scenario: health resource status is "up"
  Given url baseUrl + '/actuator/health'
  When method get
  Then status 200
  And match response == {'status':'UP'}
```


### 1. Execute the karate feature file with IntelliJ IDEA.

You can run the karate feature file directly from the editor in IntelliJ, as shown in the GIF below or use the shortcut to run the current scenario.

![Execute Karate Feature From Editor](run_karate_feature_1.gif)

You can define variables used in the tests in the test-resource file "karate-config.js". Here we set the variable *baseUrl* depending on the environment. Localhost is our default environment. Karate calls this configuration function for every scenario.

```javascript
function fn() {
    var env = karate.env; // get java system property 'karate.env'
    karate.log('karate.env system property was:', env);
    if (!env) {
        env = 'dev'; // a custom 'intelligent' default
        karate.log('karate.env set to "dev" as default.');
    }
    let config;
    if (env === 'test') {
        config = {
            baseUrl: 'https://spring-boot-karate-example.herokuapp.com'
        }
    } else if (env === 'dev') {
        let port = karate.properties['karate.port'] || '8080'
        config = {
            baseUrl: 'http://localhost:' + port
        }
    } else {
        throw 'Unknown environment [' + env + '].'
    }
    // don't waste time waiting for a connection or if servers don't respond within 0,3 seconds
    karate.configure('connectTimeout', 300);
    karate.configure('readTimeout', 300);
    return config;
}
```

This way, you can run an entire feature or a single scenario. But, this requires the application to be running. It is best for executing the feature or scenario during development to get fast feedback.


### 2. Execute scenarios with JUnit.

A second way to run the feature files is with JUnit. The following picture shows a basic JUnit test to run the karate test.

```java
@SpringBootTest(
    classes = {SpringBootKarateExampleApplication.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringActuatorFeature {

  @LocalServerPort private String localServerPort;

  @Karate.Test
  public Karate actuatorResourceIsAvailable() {
    return karateSzenario("actuator resource is available");
  }

  @Karate.Test
  public Karate healthResourceStatusIsUp() {
    return karateSzenario("health resource status is \"up\"");
  }

  private Karate karateSzenario(String s) {
    return Karate.run()
        .scenarioName(s)
        .relativeTo(getClass())
        .systemProperty("karate.port", localServerPort)
        .karateEnv("dev");
  }
}
```

The SpringBootTest is used to bootstrap the server and supply the local server port as a system property to karate. This system property is then used during the karate configuration to configure the *baseUrl* variable.

To run these tests during the integration test phase with maven, you must configure the maven-failsafe-plugin accordingly.

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <configuration>
        <includes>**/*Feature.java</includes>
    </configuration>
</plugin>
```


### 3. Execute all feature files in parallel with the deployed application.

When you want to verify your deployed application, you can run all feature files in parallel.
For this, you can also use JUnit to initialize the karate test runner and then let karate execute the features in parallel.
In the example shown below, we configure karate to use up to eight threads in parallel.
```java
public class AllKarateFeaturesWithTestDeployment {

  @Test
  public void runAllFeaturesInParallel() {
    Results results = Karate
            .run("./target/test-classes/karate")
            .karateEnv("test")
            .parallel(8);
    Assertions.assertEquals(0, results.getFailCount(), results.getErrorMessages());
  }
  
}
```

### Conclusion

In my daily work, I use all three ways.
During development, I run the feature files directly and do acceptance-test-driven development.
I use the JUnit scenario tests in the CI pipeline to prevent bugs.
To be sure that the deployed application is running as expected, the pipeline executes the "all features test" after a successful deployment.

The complete example source code is available on our [GitHub Repository](https://github.com/42talents/spring-boot-karate-example).

If you are interested to learn more about Karate and Spring Boot, [get in touch and have a look at our training courses!](https://42talents.com/en/training/in-house)

Happy coding with ❤️ from Bern.
