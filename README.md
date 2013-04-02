projectCourse
=============

5100-B3-3F13;Project course: Development studio

## Introduction ##

Currently our repository have 3 different directories, AisLibReader, VirtualWorld and eSim. The follow 3 sections will go into deeper explination what each directory/project consist and requirements.

# AisLibReader #

### Prerequisites ###

* Java 1.7
* e-Navigation https://github.com/dma-dk/e-Navigation.git
* AisLib https://github.com/DaMSA/AisLib.git

### Description ###

Our first attempt to build our own AisLib reader, which read messages from AisLib. This is needed for our virtual world, to draw ships in the virtual environment, with their real world coordinates, speed, direction and etc.

### Building ###

Build project in eclipse

# VirtualWorld #

### Prerequisites ###

* Java 1.7
* jMonkeyEngine http://jmonkeyengine.com/

### Description ###

This is currently our main focus in our project. It is our virtual environment, which will draw the virtual ship controlled by the user. Later information gathering from AisLib, and draw the ships from the real world, with the given attributes from AisLib will be implemented.

### Building ###

Build project in eclipse

# eSim #

### Prerequisites ###

* Java 1.7
* Maven 3

### Description ###

Not really used at the given time, it is an Java interface created by brainstorming all the attributes our ships needed. This was to make sure that we later on didn't miss any controls, or settings as our project grows. It can be build, but there is no constructor or methods at the given time.

### Building ###

Build both e-Navigation and AisLib:
 
    mvn install
 
And add all the jar libraries to the project.