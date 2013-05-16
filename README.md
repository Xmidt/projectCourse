projectCourse
=============

5100-B3-3F13;Project course: Development studio

## Introduction ##

Hi.

# eSim #

### Prerequisites ###

* Java 1.7
* Maven 3

### Description ###

Not really used at the given time, it is an Java interface created by brainstorming all the attributes our ships needed. This was to make sure that we later on didn't miss any controls, or settings as our project grows. It can be build, but there is no constructor or methods at the given time.

### Building ###

Build both e-Navigation and AisLib:
 
    mvn install
 
This is easier said than done when using Maven. First off start by importing eSim as a Maven project. Then add the AIS Parent and eNav Parent projects (which should be pulled and imported as projects themselves) as dependencies to the new eSim project. When this is done, go to the project folder and open the pom.xml (or edit it in the IDE) and change the absolute path to the correct one. When this is done the dependencies should be resolved.

### TODO ###

Ignore the repo directory in git when everyone has it.
Ignore the pom.xml in git when everyone has it.
Adding more library JARs is not easy. A script is made which can generate the
correct directory structure and a valid pom-entry.
