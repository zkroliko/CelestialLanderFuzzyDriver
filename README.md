# CelestialLanderFuzzyDriver

## Description

A simple controller implemented using JFuzzyLogic with Java code serving as a environment simulator.

The problem is landing a probe (or manned spacecraft) on some celestial body. 
Model includes gravity (g = 3.32) and simple atmospheric model (differential equation).

The vehicle position and inertia as well as engine impulse has been taken into account.

Vehicles reactions and engine power are fully configurable using controller.fcl file.

The graphical interface enables control of the situation itself (position,speed).

![alt tag](https://cloud.githubusercontent.com/assets/8882153/14537307/b16c76b0-0276-11e6-9f12-f782b62bd473.PNG)

## Build 

Maven attached to the project. JFuzzy library is not supported by global maven repository and is attached as a .jar.

## Run 

### Attached configurations

Running configurations are contained in IntelliJ project. They contain `PlotFcl` for debugging and `LanderApp` for the whole experience.

### Command line

Both the `PlotFcl` and `LanderApp` accept the same argument list:

`<fcl file> <vert speed> <height> <positionX> <positionY>`

*Example argument list:*

`controller.fcl 250 1200 1000 1500`
