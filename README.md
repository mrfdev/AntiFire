# 1MB AntiFire

**This is a Helper plugin for the fire-damage events we want to have better control over on the 1MoreBlock.com Minecraft
Java server, targeting PaperMC 1.21.11 and Paper build 26.1.2.**

The purpose of this plugin is two-fold:

First, it will try to help prevent damage caused by ignited fire, fire-spread, etc. while the server is running. On our
server we do not support grief, fire-spread, etc.

And secondly, we try to beat existing protection plugins during start-up. We've noticed in the past that WorldGuard with
a bit of lag and while worlds are loading and people log onto the server early can cause fire-damage from fire-spread at
loaded (spawn) chunks.

More details about installation, configuration and usage can be found in
the [wiki](https://github.com/mrfdev/AntiFire/wiki) pages.

## Origins

During the Minecraft 1.8 / 1.12.2 era I always noted issues with fire control, I wanted this feature. DefianceCoding
from the Spigot community has helped me get started with this plugin. Unfortunately, he's been quite busy and not always
available when we upgrade. Since then since the original version got updated once to move from ID numbers to material
names and work with 1.13+ again. This was version 1.x, contributions to make this happen was by the456gamer (thank you).

## Where we are now

This project has been modernized again for current PaperMC development, with the active focus now on PaperMC `26.1.2+`.
The current build targets Paper `1.21.11` and Paper build `26.1.2`, with a Java 25 toolchain and a clean Gradle build
that works from a fresh clone of the repository.

The goal for this stage is to keep the plugin small, dependable, and easy to maintain while preserving its original
purpose: stop unwanted fire spread, prevent block burn damage, and let controlled fire on netherrack continue to work
as expected. Future changes should continue to prioritize compatibility with newer PaperMC releases in the `26.1.2+`
range.

## Bugs / Suggestions

If you have an issue with this plugin, please make sure your Spigot or Paper engine is up to date, and are using the
latest build of this 1MB AntiFire plugin.

When you're sure you've done everything right, you're free
to [open an issue](https://github.com/mrfdev/AntiFire/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BBUG%5D)
and file a bug report. We do not guarantee a fix, but we will do our best.

If you have a suggestion or feature request, feel free
to [open a new discussion](https://github.com/mrfdev/AntiFire/discussions/new), and describe what you wish this plugin
would include. We can at least read it and take it under consideration.

## Wishlist

At the moment this works fine, but I can imagine in the (near) future we could add support for new 1.17 things and make
sure everything is up to date. Maybe include some startup checks.

## Credits

- [DefianceCoding](https://www.spigotmc.org/) for listening to the original issue and throwing together the starter
  version of this plugin.
- [The456gamer](https://github.com/the456gamer) for the 1.16.x update work that helped move the project forward
  again after the material-name changes.
- [Greymagic27](https://github.com/Greymagic27) for the update work for Minecraft / Paper 1.21.x.
- [mrfloris](https://github.com/mrfloris) for the update to Paper 26.1.2, the Java 25 build, and ongoing maintenance.
- OpenAI for helping put the current README and Gradle build update together.

## Changelog

### 2.0.4-023-j25-26.1.2

Commit message:

`Modernize AntiFire for Paper 26.1.2+ with Java 25 Gradle build and README refresh`

Changes in this update:

- Added Gradle build support so a fresh clone can run `gradle build` and produce the plugin jar without depending on
  the local `servers/` folder.
- Updated the build and release version to `2.0.4-023-j25-26.1.2` and aligned the jar output name with the current
  Paper / Java target.
- Kept Maven metadata aligned with the new version and Java 25 release settings.
- Updated `.gitignore` so `/servers/`, `.DS_Store`, Gradle output, and related local build files stay out of Git.
- Refreshed the README introduction and status text to reflect support for PaperMC `1.21.11` and Paper build `26.1.2`,
  with the active maintenance focus on `26.1.2+`.
- Added and updated the credits section to preserve existing acknowledgements and include
  [Greymagic27](https://github.com/Greymagic27), [mrfloris](https://github.com/mrfloris), and OpenAI.
- Updated plugin metadata and packaged version information so the built jar reports the current release correctly.

## Build

Clone the project and run:

`gradle build`

The build does not use the local `servers/` folder. The jar is written to:

`build/libs/1MB-AntiFire-v2.0.4-023-j25-26.1.2.jar`

## Version

[Tested build](https://github.com/mrfdev/AntiFire/releases) Version `2.0.4-023-j25-26.1.2`, targeting Paper
1.21.11 and Paper 26.1.2. Last updated: April 2026.
