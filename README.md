# README #

Diamonds&Hearts is a social hybrid card game with a simple ruleset and turn based gameplay. Cards are randomly generated and single use only, but they can be saved for use in a future game.

The target platform for the game is Android version >= 2.2.0.

### Building ###

Make sure Eclipse Android ADT and EGit plugin are installed, then follow these instructions within Eclipse:

* File -> Import -> Git -> Projects from Git -> Clone URI
* Enter URI of this project and authentications, then click Next
* Select master, then click Next
* Select location to put local git repo, then click Next
* Select Import Existing Projects, then click Next
* Select all projects listed, then click Finish

### Running ###
To run the Project:

* Select project DiamondsHearts
* Run As -> Android Application
* Select a phone to run with
* Click OK

### Testing ###

Test files are in project DiamondHeartsTest, which is located at tests folder of project DiamondsHearts.
For testing, make sure Eclipse Junit plugin is installed, then follow these instructions within Eclipse:

* Select project DiamondHeartsTest
* Run As -> Android Junit test
* Click OK