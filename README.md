# Spring Boot (Modified) Tutorial – Facilitator Guide

## 1. Purpose

Walk through a simple development exercise that demonstrates several general good development practices.

## 2. Resources

•	Online tutorial page: http://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-first-application.html

•	PowerPoint deck: spring-boot-tutorial.pptx

## 3. Overview

TBD

## 4. Sequence

Start with the deck. The first few slides suggest a goal for the exercise. Take a look at the speaker notes and adjust them according to your own preferences as a guide/mentor.

After the slide that gives particants the URL to the tutorial, switch to this document and follow the sequence of actions below.

### 4.1. Necessary software

Participants will need the following software installed on their machines:

* Java JDK version 1.8
* Maven 3.3 or higher
* Git client
* A text editor (any)
* A Java-aware IDE (if desired)

### 4.2. Getting started

Follow the tutorial section 11.1 to create the ```pom.xml``` file using the command line. Use a text editor, don’t use an IDE yet.

Run ```mvn package``` and ```mvn dependency:tree``` as described in the tutorial.

Explain what a pom is, if necessary, and explain the parent pom named sprint-boot-starter-parent.

Follow the tutorial section 11.2 to add the dependencies to the pom. Run ```mvn dependency:tree``` again as described in the tutorial instructions.

### 4.3. Examining the sample code

Section 11.3 of the tutorial is where we will diverge from the instructions. 

Display the sample Java source code in the tutorial. It looks like this:

```java
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Example {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }
}
```

Lead a discussion with the group to see if they can identify any issues with this code. If they need a hint, mention _separation of concerns_.

Here's what we're looking for (according to this author, anyway):

The ```main``` method, the "Hello" functionality, and the RESTful service wrapper code are three different _concerns_. In a "real" application, we would not combine these concerns in the same class. For this exercise, we're going to separate these concerns right from the start.

Ask the group if they can see any other potential problems with the code. 

The second thing we're looking for (at least, according to this author) is the fact there are no unit tests; the code is not _test-driven_.

As conscientious developers, we would normally test-drive any new code. Therefore, we're going to do that for this exercise right from the start.

One final issue is that the sample code uses the _default package_. In a "real" application, all Java classes would belong to a package. We're going to start with the default package and refactor the first Java class into a proper package after we've migrated everything into an IDE. For the moment, don't worry about the package.

### 4.4. Creating the Maven directory structure

We're still working on the command line, without using an IDE that "knows" how to create a Maven project automatically.

Create the usual Maven source directories:

```shell
mkdir -p src/main/java
mkdir -p src/test/java
```

Or on Microsoft Windows:

```shell
mkdir src\main\java
mkdir src\test\java
```

On Microsoft Windows only, if command extensions are not enabled on your system, you can write a short script to create directory trees, like this:

```shell
@echo off
setlocal enableextensions
md %1
endlocal
```

### 4.5. Setting up version control

The basis of good development practices such as test-driven development, continuous integration, and continuous deployment is _version control_. Guide participants in creating a git repo on ```github.com```.

Once that has been set up, open a command line window and navigate to the project root directory. Set up a local git repo for the project:

```shell
git init
git config --global user.name "Your Name"
git config --global user.email "youremail@wherever.com"
git remote add origin master [url]
git add .
git commit -m "Initial commit"
```

### 5. Starting development

Now we're set up sufficiently to begin writing some code.

### 5.1. A little up-front planning

Now guide the group through the process of deciding _how_ to separate the concerns in this application:

* Core "business" functionality (in this case, saying "Hello")
* RESTful service wrapper code  
* ```main``` method to run the application as a Java application

Granted, the three pieces will be very small, and this might appear to be a bit pointless to some of the participants. The key idea is that if we _start_ by doing things right, we'll avoid headaches later.

### 5.2. Test-driving the core functionality

Let's begin with the core "business" functionality. The all-important ability to say "Hello" represents a significant competitive advantage for our company. We must be sure to build it well.

The next aspect of sound software development methods we will include in the exercise is _test-driven development_. Here's how that practice is depicted on the C2 wiki:

![TDD](images/tdd.png "Test-Driven Development")

As the diagram implies, we'll start by writing a test. It's a very small test case that expresses a bit of the expected behavior of the application we're building. We call it a _microtest_.

We're going to use ```JUnit``` as the automated testing framework for this exercise. To add this to the Maven POM as a dependency, add the following definition to the ```pom.xml``` file:

```shell
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
```

Now create a Java class file, ```HelloTest.java```, in the ```src/test/java``` directory. (We're still working with a simple text editor.) Make it something like this:

```java
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HelloTest {    
    @Test
    public void itSaysHello() {
        Hello hello = new Hello();
        assertEquals("Hello, World!", hello.greet());
    }
}
```

Walk participants through the test class and explain it. The method ```itSaysHello``` expresses one discrete bit of application behavior. We assume there will be an object of type ```Hello``` that will contain a method named ```greet``` which is expected to return the string, "Hello, World!" when called.

Now run:

```shell
mvn test
```

You'll see errors indicating the symbol ```Hello``` could not be found, like this:

```shell
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.1:testCompile (default-testCompile) on project myproject: Compilation failure: Compilation failure:
[ERROR] /Users/dave/Documents/Projects/springboot-tutorial/src/test/java/HelloTest.java:[7,9] cannot find symbol
[ERROR] symbol:   class Hello
[ERROR] location: class HelloTest
```

We're now experiencing test-driven development. The _feedback_ from our microtest is telling us to create a Java class named ```Hello```. Who are we to argue?

Create a file named ```Hello.java``` in directory ```src/main/java``` with the following contents:

```java   
public class Hello {
    public String greet() {
        return "Hello, World!";
    }
}
```

Now let's try running the tests again:

```shell  
mvn tests
```

This time we see output like this:

```shell  
Results :
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------
[INFO] Total time: 2.722 s
[INFO] Finished at: 2016-11-15T16:26:38-06:00
[INFO] Final Memory: 24M/217M
```

People often speak of the _red-green-refactor_ cycle of TDD. Our failing microtest was the _red_ step. Now we've made that microtest pass. That's the _green_ step. 

The third step involves examining the code we've just written to see if it could be simplified. At the moment, there's nothing to refactor, as we haven't build up any _cruft_ in the code. 

We want to establish the habit of examining our code every time we reach _green_ so we can keep the design clean at all times. For now, we'll agree as a team, explicitly, that there's no refactoring to be done at this time.

### 5.3. Committing to version control

Continuous integration (CI) means pushing small changes frequently and checking for integration problems. 

To keep things simple, it's a generally-accepted good practice for all team members to work on a single branch of the source code. The general procedure for committing code with this approach is:

1. Run all tests locally
1. Update local from head
1. Run all tests locally 
1. Push to origin (master branch)
1. Check feedback from the CI build and fix if broken
 


 

