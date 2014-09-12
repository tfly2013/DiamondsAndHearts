# README #

Diamonds&Hearts is a social hybrid card game with a simple ruleset and turn based gameplay. Cards are randomly generated and single use only, but they can be saved for use in a future game.

The target platform for the game is Android version >= 2.2.0.

### Build Instructions ###

Check out the project using git, make sure Eclipse Integration Gradle and Android ADT plugin are installed, then follow these instructions within Eclipse:

* File -> Import -> Gradle -> Gradle Project.
* Click Build Model.
* Select all project listed.
* Click Finish.

### Testing ###

Test files are located at diamonds_hearts-core/src/com.diamondshearts.tests.
For testing, make sure Eclipse Junit plugin is installed, then follow these instructions within Eclipse:

* Select project diamonds_hearts-core
* Run As -> Junit test
* Tick Use configration specific settings
* Select Eclipse Junit launcher
* Click OK