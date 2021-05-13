![MCreator Link](https://www.pylo.co/static/mcreator/link/link_small.png?)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/Pylo/MCreatorLink/blob/master/LICENSE) [![Documentation](https://img.shields.io/badge/java-doc-green.svg)](https://pylo.github.io/MCreatorLink/) [![Build Status](https://travis-ci.com/Pylo/MCreatorLink.svg?branch=master)](https://travis-ci.com/Pylo/MCreatorLink)

MCreator Link enables you to connect hardware devices such as Arduino and Raspberry Pi with Minecraft game via MCreator procedures, commands and general API for Minecraft mod developers.

Setup instructions can be found on the MCreator Link official website: https://mcreator.net/link

![MCreator Link](https://www.pylo.co/static/mcreator/link/demolight.gif?)

## Concept

![MCreator Link Concept](https://www.pylo.co/static/mcreator/link/diagramnobg2.png?)

## Implementations

Here are links to the current implementation of device support for the MCreator Link:

### [MCreator Link for Arduino](https://github.com/Pylo/MCreatorLinkArduino) 
[![Build Status](https://travis-ci.com/Pylo/MCreatorLinkArduino.svg?branch=master)](https://travis-ci.com/Pylo/MCreatorLinkArduino)

### [MCreator Link for Raspberry Pi](https://github.com/Pylo/MCreatorLinkRaspberryPi) 
[![Build Status](https://travis-ci.com/Pylo/MCreatorLinkRaspberryPi.svg?branch=master)](https://travis-ci.com/Pylo/MCreatorLinkRaspberryPi)

## Contributing

You are welcome to support this project by opening pull requests.

Before we can use your code, you must sign the [MCreator CLA](https://cla-assistant.io/Pylo/MCreatorLink), which you can do online. The CLA is necessary mainly because you own the copyright to your changes, even after your contribution becomes part of our codebase, so we need your permission to use and distribute your code. We also need to be sure of various other things—for instance that you'll tell us if you know that your code infringes on other people's patents. You don't have to sign the CLA until after you've submitted your code for review and we approved it, but you must do it before we can put your code into our codebase.

## Development

To contribute to MCreator Link, clone this repository and setup this workspace as you would do
with any Minecraft mod. We recommend to use Intelij IDEA for the development.

To clone the submodules too, use:

`git clone --recursive https://github.com/Pylo/MCreatorLink.git`

Then export the MCreator Link distribution, use

`gradlew exportAll`

Otherwise use as normal Forge Gradle mod project.

## Notice

NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG.
