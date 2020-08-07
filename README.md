# BEING RECODED
# Hades Anti-Cheat

## Overview
Hades is an advanced anticheat with many checks designed to patch various cheats/hacks. The goal of this project is to provide a free anticheat solution for servers that automatically punishes any player that uses any form of cheat. This project will always be open-sourced and free, so you can use this anticheat on your server and modify it to fit your server needs. None of the code is stolen from other online sources or people without being credited. Since this project is rather new, expect bugs & many things to be labeled untested.

**Compatibility:**

Spigot 1.7.x

Spgiot 1.8.x

**Untested Compatibility:**

Spigot 1.9.x

Spigot 1.10.x

Spigot 1.11.x

Spigot 1.12.x

Spigot 1.13.x

Spigot 1.14.x

Spigot 1.15.x

**Other spigot forks may not work with Hades.**

## Support
Project is open-sourced and therefore all support will be handled on issues page.
You can also join my discord here. (https://discord.gg/zURutBu)

## Detections
**Combat:**

AimAssist

KillAura

AutoClicker

Criticals

Reach (unstable)

Velocity (unstable)

**Movement:**

Fly

Motion

Scaffold

SmallHop

Speed

**Packet:**

BadPackets

NoFall

Timer

## Features
**High Performance:**

Hades is packet-based, meaning it runs completely asynchronously, dropping almost no TPS on your server. The project is also being optimized with every commit/update.

**Advanced Detections:**

Hades has hard-coded checks that are tested against various hacks from many clients. These checks are always being updated to maintain little false positives but still detecting a vast majority of cheaters.

**AutoPunish:**

Hades comes with an automatic punishment feature. When a player goes over the max violations for a certain check (customizable in config), it will carry out the punishment set in the config.

## Restrictions
**Dependencies:**

None!

**Incompatibilities:**

Any plugin that modifies vanilla mechanics (untested)

PlugMan Reload (incompatible)

Custom Enchants (untested)

Spigot forks (such as PaperSpigot) (untested)

## Commands
**Commands:**

/hades - hades.command

/hades gui - hades.command.gui

/hades info - hades.command.info

/alerts - hades.command.alerts

/logs - hades.command.logs

## Permissions

hades.alerts - receive notifications

## Usage
**Installation from Jar:**

Just simply download the latest Hades.jar file from from the releases tab, drag it into your server 'plugins' folder, and start your server.

**Installation from Source:**

Start by cloning or downloading the source from the Hades GitHub Repository. You will now need a Java IDE to build the project into a plugin jar. Download and install Eclipse (or any prefered Java IDE) and open the IDE after installation. Import the Hades files as a maven project and allow your IDE to automatically download all of the maven dependencies. Once your IDE has finished downloading the dependencies, simply build the project, drag the outputted jar file into your server's 'plugins' directory, and start your server.

## Tos
**License:**

This project is licensed under the GNU General Public License (GPL) 2.0. View the project license on the license tab or in the LICENSE file.
