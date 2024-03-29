projectCourse
=============

5100-B3-3F13;Project course: Development studio
This is the repository for team Echo.

## Introduction ##

eSim is a basic (but growing) maritime 3D simulator. The aim is using the AIS transponders on ships in the read world to make a 3D environment populated with virtual copies of real ships mixed with
true virtual ships. The 3D graphical engine used is the jMonkeyEngine (http://jmonkeyengine.org)

# eSim #

### Prerequisites ###

* Java 1.7
* Maven 3
* e-Navigation https://github.com/dma-dk/e-Navigation.git
* AisLib https://github.com/DaMSA/AisLib.git

### Description ###

This is the Maven project containing the current stage of the simulator. The graphical engine is contained in a local Maven repository as the jMonkeyEngine does not at this time provide an external Maven repository.

### Building ###

Currently the easiest way to build is using either Eclipse or NetBeans:
First off start by importing eSim as a Maven project. Then add the AIS Parent and eNav Parent projects (which should be pulled and imported as projects themselves) as dependencies to the new eSim project. It might be necessary to add the packages in the Parents individually. When this is done the dependencies should be resolved.

### Run ###
If the application runs correctly a new window should appear, with a blue plane and a skybox.
The world may be populated by other ships depending on the location of the virtual ship in the virtual world and if any AIS data is available. 
The X key toggles between follow camera on the ship, and freecam. Pressing V will toggle the FilterPostProcessor which turns the blue surface into pretty water.
Depending on the running system, this water may cause the application to crash or result in a black screen. Pressing V again should restore the blue surface. 

Controlling the ship can be done with U,J,K,H (pressing these multipile times is needed).
U increases the speed and J decrese the speed. H and K turn the rudder, which results in the ship/boat turning depending on speed and how much the rudder is turned.
Pressing Y resets the rudder to a middle position.

### TODO ###

Ignore the repo directory in git when everyone has it.
Ignore the pom.xml in git when everyone has it.
Adding more library JARs is not easy. A script is made which can generate the
correct directory structure and a valid pom-entry.
