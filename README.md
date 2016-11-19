# TDD/CI/CD Walkthrough Facilitator's Guide

This project supports part 1 of a 3-part demonstration/walkthrough/dojo exercise. By the end of the series, you'll have a reusable jar containing the "Hello, World!" functionality, a standalone Java app, a RESTful microservice, and a working CI/CD pipeline.

* Part 1 - http://github.com/neopragma/javahellolib 
* Part 2 - javahelloapp (You are here)
* Part 3 - http://github.com/neopragma/javahelloservice

## 14. Purpose and overview

In [part 1 of the walkthrough](http://github.com/neopragma/javahellolib  "Part 1"), we touched on several activities that occur as part of a software delivery pipeline:

* Using Maven for Java projects
* Separation of concerns
* Using a version control system
* Single branch strategy
* Setting up continuous integration
* Using an IDE
* Packaging a reusable jar to be uploaded to a repository
* Test-driving application code through microtests
* Benefits of frequent commits

In part 2 (this project), we'll address the following aspects:

* Using Spring Boot with Maven 
* Packaging applications for deployment
* Setting up continuous deployment
* Writing a standalone Java application suitable for cloud deployment

Still to come:

* Writing a RESTful microservice in Java suitable for cloud deployment
* Organizing an automated test suite

## 15. Resources

•	Online tutorial page: http://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-first-application.html


## 16. Write a driver for a standalone Java application

Let's get busy! 

We're going to develop a standalone Java application that uses the ```javahellolib``` jar we created in part 1 of the walkthrough. If you worked through that part, then you have the jar in the local Maven repository, located at ```~/.m2/repository``` on your development system. We didn't upload it to Maven Central, as it's just a toy project for demonstration, practice, and reference.

### 16.1 The POM

We want to use Spring Boot to help us package the standalone application for deployment. Our POM will be different from the one we used for the ```javhellolib``` project. Here it is:

```xml 
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.neopragma.javahelloapp</groupId>
    <artifactId>hello</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.4.2.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
    </dependencies>    
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>   
 
</project>
```

Notice this POM declares a _parent_ POM. It's _spring-boot-starter-parent_, which declares components of Spring Boot that are helpful for creating and packaging applications in a way suited to cloud deployment.

### 16.2. The Main class

You may recall we identified three _concerns_ in the sample code provided in the Spring Boot Tutorial:

* The "Hello, World!" functionality
* The wrapper for the standalone version
* The wrapper for the RESTful service version

Just now we're addressing the wrapper for the standalone version. It will consist of a Java class that contains a ```main``` method, configured so that the Spring framework can start it.

Should we test-drive this class? Some would say we should test-drive every line of production code we write, no matter what. Others would say this sort of code is basically boilerplate. All it will do is instantiate a ```Hello``` object and call its ```greet()``` method, which takes no arguments. It will also write the output from ```greet()``` to ```stdout```. 

None of these operations represents unique application logic that we are hand-coding. It all amounts to wiring up some functionality provided by the Java compiler or the JVM. Everything it does will be validated at the integration test level; there's no finer-grained behavior to check at the unit level. Let's just write this one.

```java  
package com.nepragma.javahelloapp;
public class Main {	
	static Hello hello;
	public static void main(String[] args) {
		hello = new Hello();
		System.out.println(hello.greet());
		System.exit(0);
	}
}
```

Show them how to run this from inside the IDE and from a command line.

### 11.2. Create an executable jar

The way to package a standalone Java application is as an _executable jar_. Let's do that using Maven.

One of the advantages of using Spring Boot is that it comes with a well-implemented Maven plugin to build executable jars, ```spring-boot-maven-plugin```. It works better than the usual ```maven-assembly-plugin``` or ```maven-jar-plugin``` that you may have used in the past, or the old ```maven-antrun-plugin``` workaround to run an ```ant``` task out of Maven. The plugin is declared in the sample POM shown above.

You can create the executable jar from the command line like this:

```shell
mvn package
```

You can also build it from within the IDE. Spring Tool Suite comes with several Maven run configurations predefined, but they don't include one for ```mvn package```. Show participants how to create a new run configuration.

![Maven package](images/mvn-package-run-config.png "Maven package")

Note that by using the Springboot Maven plugin, you've made the application compatible with Cloud Foundry. Show participants the project properties in Spring Tool Suite:

![Properties](images/cloud-foundry-standalone.png "Properties")

This doesn't mean we _must_ deploy to Cloud Foundry. It's only a convenience built into Spring Tool Suite to support Cloud Foundry. In fact, we'll be deploying to Heroku later in this exercise (because it's free).

### 11.3. Run the application

Now you can use that run configuration to create the executable jar from within the IDE using the "Run as..." option from the context menu.

To execute the resulting application from the command line, use:

```shell
java -jar target/javahelloapp-0.0.1-SNAPSHOT.jar
```

To execute it from within the IDE, open the context menu and choose Run as... Java application.

### 11.4 Create a script to run the application

It's not very convenient to type the ```java``` command to run the application. It would be easier if we wrapped the command in a script with an easy-to-remember name, like ```run```. The script is just a one-liner: The ```java``` command. Remember to give it execute permission.


### 11.5 Create a .gitignore file

When we imported the project into Spring Tool Suite, some IDE-specific files were created in the project directory that we don't want to store in version control. 

The IDE doesn't show hidden files by default. You can make it do so, but it isn't very intuitive. Show participants how to do this. 

First, locate the nearly-invisible drop-down menu button near the upper right-hand corner of the Package Explorer tab.

![Explorer menu](images/explorer-menu-icon.png "Package Explorer menu")

Choose Filters... to open a dialog where you can control which files appear in Package Explorer. 

![Filters](images/java-element-filters.png "Filters")

Instead of choosing which files to display, you're choosing which files _not_ to display. The entry ```.* resources``` is already checked. Uncheck it to make the IDE show hidden files in Package Explorer.

Show participants how to create a ```.gitignore``` file (or edit the one Springboot generated) to control which files will be committed to version control.

To prevent unwanted files from being stored in git, we put the following entries in ```.gitignore```:

```shell  
.DS_Store
target
.classpath
.project
.settings
*~
```

If you're using another operating system, you might see temporary files specific to that system that you'll want to include in ```.gitignore``` as well. For example, when you edit a file on Ubuntu Linux using the default text editor, it creates a temporary file with a tilde on the end of its name. Editing a file named ```MyClass.java``` will cause another file to be created, named ```MyClass.java~```. 

You _do_ want the ```.gitignore``` file itself to be maintained under version control.

### 11.6 Commit, push, and build

Now commit and push the changes and watch the build run in Travis CI.

## 12. Setting up continuous deployment

Remind participants of the canonical delivery pipeline and/or the diagram showing loops within loops. We're going to extend the pipeline for our tutorial exercise to include automated deployment.

Go to Heroku and define the tutorial app there, using your credentials or setting up new credentials as you please. This will be a _one-off dyno_ app to execute the standalone Java application we just built.

Use the Travis and Heroku command line tools to create an encrypted API key for Heroku, like this:

```shell  
travis encrypt $(heroku auth:token) --add deploy.api_key
```

Edit the ```.travis.yml``` file and add a ```provider``` specification. The file should now look something like this:

```shell
language: java
jdk:
- oraclejdk8
deploy:
  provider: heroku
  api_key:
    secure: [a long encrypted value appears here]
```

Now when you commit and push the modified ```.travis.yml``` file to Github, the build will start on Travis CI and (assuming it worked) the application will be deployed to Heroku.

Because this is a standalone application that runs in a one-off dyno, it will not start automatically and it will not remain operational when you execute it. Show the group how to run such an app on Heroku:

```shell
heroku run bash --app javahelloapp
~ $ java -jar target/javahelloapp-0.0.1-SNAPSHOT.jar
Hello, World!
```

## 13. Where do we stand?

We've looked at several good development practices so far:

* Using Maven for Java projects
* Separation of concerns
* Using a version control system
* Single branch strategy
* Setting up continuous integration
* Using an IDE
* Packaging a reusable jar to be uploaded to a repository
* Test-driving application code through microtests
* Benefits of frequent commits
* Using Spring Boot with Maven 
* Packaging applications for deployment
* Setting up continuous deployment
* Writing a standalone Java application suitable for cloud deployment
