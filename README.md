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

Build project in eclipse/netbeans:
Make a new project with the jMonkey libraries added as dependencies and use the Water.java file as the main file (change package structure if needed).

### Run ###
If the application runs correctly a new 1024x768 window should appear, with a blue plane and a skybox.
Pressing Space should spawn a ship, and Enter will spawn a few objects in the world around the ship.
The X key toggled between follow camera on the ship, and freecam. Preesing V will toggle the FilterPostProcessor which turns the blue surface into pretty water.
Depending on the running system, this water may cause the application to crash or result in a black screen. Pressing V again should restore the blue surface. 

Controlling the ship can be done with U,J,K,H (pressing these multipile times is needed).
U increases the speed and J decrese the speed. H and K turn the rudder, which results in the ship/boat turning depending on speed and how much the rudder is turned.
Pressing Y resets the rudder to a middle position.

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
