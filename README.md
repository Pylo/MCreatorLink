![MCreator Link](https://www.pylo.co/static/mcreator/link/link_small.png?)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/Pylo/MCreatorLink/blob/master/LICENSE) [![Documentation](https://img.shields.io/badge/documentation-available-green.svg)](https://pylo.github.io/MCreatorLink/) [![Build Status](https://travis-ci.com/Pylo/MCreatorLink.svg?branch=master)](https://travis-ci.com/Pylo/MCreatorLink)

MCreator Link enables you to connect hardware devices such as Arduino and Raspberry Pi with Minecraft game via MCreator procedures, commands and general API for Minecraft mod developers.

Setup instructions can be found on the MCreator Link official website: https://mcreator.net/link

![MCreator Link](https://www.pylo.co/static/mcreator/link/animation.gif?)

## Concept

![MCreator Link Concept](https://www.pylo.co/static/mcreator/link/diagramnobg2.png?)

## Issue tracker

Issues can be reported on the official MCreator development team issue tracker
found on https://mcreator.net/tracker

## Implementations

Here are links to the current implementation of device support for the MCreator Link:

### [MCreator Link for Arduino](https://github.com/Pylo/MCreatorLinkArduino) 
[![Build Status](https://travis-ci.com/Pylo/MCreatorLinkArduino.svg?branch=master)](https://travis-ci.com/Pylo/MCreatorLinkArduino)

### [MCreator Link for Raspberry Pi](https://github.com/Pylo/MCreatorLinkRaspberryPi) 
[![Build Status](https://travis-ci.com/Pylo/MCreatorLinkRaspberryPi.svg?branch=master)](https://travis-ci.com/Pylo/MCreatorLinkRaspberryPi)

## Development

To contribute to MCreator Link, clone this repository and setup this workspace as you would do
with any Minecraft mod. We recommend to use Intelij IDEA for the development.

To clone the submodules too, use:

`git clone --recursive https://github.com/Pylo/MCreatorLink.git`

To setup workspace, set Gradle version to Gradle 3.0

`gradlew --wrapper 3.0`

Then setup the Minecraft Mod workspace:

`gradlew setupDecompWorkspace`

## Notice

NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG.
