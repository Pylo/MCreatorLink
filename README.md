![Minecraft Link](https://www.pylo.co/static/mcreator/link/link_small.png)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/Pylo/MinecraftLink/blob/master/LICENSE) [![Documentation](https://img.shields.io/badge/documentation-available-green.svg)](https://pylo.github.io/MinecraftLink/) [![Build Status](https://travis-ci.com/Pylo/MinecraftLink.svg?branch=master)](https://travis-ci.com/Pylo/MinecraftLink)

Minecraft Link enables you to connect hardware devices such as Arduino and Raspberry Pi with Minecraft game via MCreator procedures, commands and general API for Minecraft mod developers.

![Minecraft Link Concept](https://www.pylo.co/static/mcreator/link/diagramnobg.png)

Setup instructions can be found on the Minecraft Link official website: https://mcreator.net/link

## Issue tracker

Issues can be reported on the official MCreator development team issue tracker
found on https://mcreator.net/tracker

## Implementations

Here are links to the current implementation of device support for the Minecraft Link:

### [Minecraft Link for Arduino](https://github.com/Pylo/MinecraftLinkArduino) 
[![Build Status](https://travis-ci.com/Pylo/MinecraftLinkArduino.svg?branch=master)](https://travis-ci.com/Pylo/MinecraftLinkArduino)

### [Minecraft Link for Raspberry Pi](https://github.com/Pylo/MinecraftLinkRaspberryPi) 
[![Build Status](https://travis-ci.com/Pylo/MinecraftLinkRaspberryPi.svg?branch=master)](https://travis-ci.com/Pylo/MinecraftLinkRaspberryPi)

## Development

To contribute to Minecraft Link, clone this repository and setup this workspace as you would do
with any Minecraft mod. We recommend to use Intelij IDEA for the development.

To clone the submodules too, use:

`git clone --recursive https://github.com/Pylo/MinecraftLink.git`

To setup workspace, set Gradle version to Gradle 3.0

`gradlew --wrapper 3.0`

Then setup the Minecraft Mod workspace:

`gradlew setupDecompWorkspace`
