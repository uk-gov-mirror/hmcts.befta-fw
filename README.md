# BACK END FUNCTIONAL TEST AUTOMATION FRAMEWORK

## LICENSE

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## WHAT IS BEFTA FRAMEWORK?
BEFTA Framework is a framework for automation of functional tests for http-based APIs. It uses Cucumber and Rest Assured frameworks and supports a BDD (Behaviour-Driven Development) approach to software development.


## FEATURES AND CONVENIENCES PROVIDED
It provides the following functionalities and conveniences:
1.  A Domain Specific Language (DSL), which is specific to the domain of automation of http-based APIs, to describe the functional/behavioural requirements of APIs.
2.  Underlying programmatic logic to make the DSL an executable script language to execute functional tests.
3.  Flexible and efficient, JSON-based test data repository enabling test automators to define and maintain their test data with maximum re-use and minimum duplications.
4.  Fast-performing, full-range response verification, robust mechanism to assert full details of responses including response statuses, reason phrases, headers and response bodies.
5.  Dynamic data configuration means allowing dynamic & run-time-specific test data to be identified without any programming at all
6.  Custom-programmable dynamicity to allow for programmable injections into test logic
7.  Clearly designed way of adapting and integrating the framework into various API suites for any new functional test suit.
8.  Reporting, and for-diagnostic outputting
9.  Custom extensibility of out-of-the box DSL (Cucumber)

## HOW TO SETUP & INTEGRATE

### System Requirements
* System Resources (Memory, Disk, CPU) - Same for a JDK 8 installation.
  [Click here to see Oracle's reference for this](https://docs.oracle.com/javase/8/docs/technotes/guides/install/windows_system_requirements.html)

### Software Requirements
* Java SE Development Kit 8 (JDK 8)
* Your Favourite IDE
* Gradle 4.10+

### Setting Up Environment
1. Install JDK 8 or higher
2. Install a command line terminal application

#### Run BEFTA Framework Without a Build Tool
1. Download a copy of BEFTA Framework (say, version 1.2.1) in a local folder, say the root directory of an 
   IDE project in which you (will) have your automated functional tests.
2. Open your preferred terminal and change current directory to the root directory 
   of your test automation project.
3. java -cp befta-fw-1.2.1.jar uk.gov.hmcts.befta.BeftaMain 'my-feature-files/are/here, and/here, and-also/there'
   This will run the test scenarios under the local folder you specify.

#### Run BEFTA Framework With Gradle
1. Install Gradle 4.1 or higher. You can simply copy a gradle wrapper from `https://github.com/hmcts/befta-fw`.
2. Add the following dependency to your build.gradle file:
   `testCompile group: 'uk.gov.hmcts', name: 'befta-fw', version: '1.2.1'`
3. Add a javaExec section to wherever you want a functional test suit to be executed, 
   like below:
   ```
           javaexec {
            main = "uk.gov.hmcts.befta.BeftaMain"
            classpath += configurations.cucumberRuntime + sourceSets.aat.runtimeClasspath + sourceSets.main.output + sourceSets.test.output
            args = ['--plugin', "json:${projectDir}/target/cucumber.json", '--tags', 'not @Ignore', '--glue',
                    'uk.gov.hmcts.befta.player', 'my-feature-files/are/here, and/here, and-also/there']
        }
   ```
   You can place this block inside the
   ```
   task functional(type: Test) {
      ...
   }
   ```
   of your test automation project.

#### Observe Cucumber Report
1. Open in your web browser the local Cucumber report file: ./⁨target⁩/cucumber⁩/⁨cucumber-html-reports⁩/overview-features.html

#### Integrate Functional Test Suite Executions into Pipelines
We assume you will have build pipelines making gradle calls to run your automated 
functional tests which is the case for HMCTS Reform programme as seen in the open source 
repositories. However, with the simple means of test suite executions provided above, test 
suite executions can be integrated into build pipelines using Maven or any other tools 
as well. When it comes to BEFTA, test suite execution is a simple Java call to run 
a Main class with runtime arguments to specify where the features are, where the step 
implementations are and scenarios with which tags to pick up and run. You can skip 
all runtime arguments to this Main class, in which case the default arguments will 
be:
```
'--plugin', "json:${projectDir}/target/cucumber.json", '--tags', 'not @Ignore', '--glue', 'uk.gov.hmcts.befta.player', 'src/aat/resources/features'
```

#### Sample Repositories

So, below are a few github repositories into which BEFTA Framework 
has been successfully integrated:
Some Spring Boot Applications:
*
*
*

Some Node.js Applications:
*
*


## HOW TO DEVELOP A SIMPLE AUTOMATED SCENARIO



## Low-level Design
BEFTA Framework has a low-level design containing components and their interactions as depicted in the below diagram.
![Below is LLD](documentation/LLD.jpg)