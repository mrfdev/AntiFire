# 1MB AntiFire

**This is a Helper plugin for the fire-damage events we want to have better control over on the 1MoreBlock.com Minecraft 1.16 Java server.**

The purpose of this plugin is two-fold:

First, it will try to help prevent damage caused by ignited fire, fire-spread, etc. while the server is running. On our server we do not support grief, fire-spread, etc. 

And secondly, we try to beat existing protection plugins during start-up. We've noticed in the past that WorldGuard with a bit of lag and while worlds are loading and people log onto the server early can cause fire-damage from fire-spread at loaded (spawn) chunks.

More details about installation, configuration and usage can be found in the [wiki](https://github.com/mrfdev/AntiFire/wiki) pages.

## Origins

During the Minecraft 1.8 / 1.12.2 era I always noted issues with fire control, I wanted this feature. DefianceCoding from the Spigot community has helped me get started with this plugin. Unfortunately, he's been quite busy and not always available when we upgrade. Since then since the original version got updated once to move from ID numbers to material names and work with 1.13+ again. This was version 1.x, contributions to make this happen was by the456gamer (thank you).

## Where we are now

With Minecraft version 1.17 around the corner, I wanted to pick things up again for some projects. Including this one. I've updated it slightly, so it works _okay_ with 1.16.5, and the latest Spigot/Paper server engine. The next step is making it a bit more modern, follow the logic of its purpose a bit more. And prepping it for future features that I want to consider.

While it's great that it's API wasn't set, and is for java8, so it should work fine with java11/java16, etc. I kind of wanted to clean up the code, follow better logic, tweak some things, update it to java16, and make it specific for 1.16.5 so there's no start-up 'legacy material' warning, etc.

## Bugs / Suggestions

If you have an issue with this plugin, please make sure your Spigot or Paper engine is up to date, and are using the latest build of this 1MB AntiFire plugin. 

When you're sure you've done everything right, you're free to [open an issue](https://github.com/mrfdev/AntiFire/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BBUG%5D) and file a bug report. We do not guarantee a fix, but we will do our best.

If you have a suggestion or feature request, feel free to [open a new discussion](https://github.com/mrfdev/AntiFire/discussions/new), and describe what you wish this plugin would include. We can at least read it and take it under consideration. 

## Wishlist

At the moment this works fine, but I can imagine in the (near) future we could add support for new 1.17 things and make sure everything is up to date. Maybe include some startup checks. 

## Other contributions

1.16.x Update contributions from [The456gamer](https://github.com/the456gamer) (thank you so much!)

And an honorable mention for DefianceCoding for listening to my issue and throwing a starter version of this plugin together for me. I have left you in the plugin.yml as author out of respect of course.

## Version

[Tested build](https://github.com/mrfdev/AntiFire/releases) Version 2.0.1, for Spigot / Paper 1.16.5. Last updated: May 2021.
