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

At the moment this works fine, but future updates could add more PaperMC `26.1.2+` compatibility checks, extra startup
safety checks, and additional quality-of-life controls for temporary fire behavior.

## Credits

- [DefianceCoding](https://www.spigotmc.org/) for listening to the original issue and throwing together the starter
  version of this plugin.
- [The456gamer](https://github.com/the456gamer) for the 1.16.x update work that helped move the project forward
  again after the material-name changes.
- [Greymagic27](https://github.com/Greymagic27) for the update work for Minecraft / Paper 1.21.x.
- [mrfloris](https://github.com/mrfloris) for the update to Paper 26.1.2, the Java 25 build, and ongoing maintenance.
- OpenAI for helping put the current README and Gradle build update together.

## Changelog

### 2.0.5-030-j25-26.1.2

Commit message:

`Fix antifire subcommand tab completion`

Changes in this update:

- Fixed `/_antifire` tab completion so the first suggestion step now offers `debug`, `reload`, `toggle`, and `help`.
- Kept partial matching for subcommands and toggle values so completions continue to narrow as you type.
- Bumped the release to `2.0.5-030-j25-26.1.2`.

### 2.0.5-029-j25-26.1.2

Commit message:

`Clarify antifire help output and permission details`

Changes in this update:

- Expanded `/_antifire help` so it now lists every admin command with a short description.
- Added the `onembantifire.admin` permission note directly to the help output, while keeping console access available.
- Updated the README command section so the permission requirement is documented more explicitly alongside the admin commands.
- Bumped the release to `2.0.5-029-j25-26.1.2`.

### 2.0.5-028-j25-26.1.2

Commit message:

`Rename antifire status to debug and make bare command show help`

Changes in this update:

- Replaced `/_antifire status` with `/_antifire debug` to match the naming used by the other 1MB plugins.
- Changed bare `/_antifire` so it now defaults to the help output instead of dumping the current config.
- Updated the command help, command usage text, and README examples to match the new admin flow.
- Bumped the release to `2.0.5-028-j25-26.1.2`.

### 2.0.5-027-j25-26.1.2

Commit message:

`Remove antifire alias and polish admin command output`

Changes in this update:

- Removed the plain `/antifire` alias so the plugin now exposes only `/_antifire` and its namespaced variant.
- Improved `/_antifire status` with grouped sections for protection, temporary fire, tracked ignite sources, and logging.
- Improved `/_antifire reload` and `/_antifire toggle` responses so they confirm what changed in a more admin-friendly way.
- Added `/_antifire help` as a clearer built-in command summary.
- Bumped the release to `2.0.5-027-j25-26.1.2`.

### 2.0.5-026-j25-26.1.2

Commit message:

`Allow console access to AntiFire admin command while keeping player permission checks`

Changes in this update:

- Added an explicit `canUse(...)` check for the Paper `BasicCommand` so the server console can always use `/_antifire`.
- Kept player access restricted to `onembantifire.admin`, so operators still do not get access by default.
- Bumped the release to `2.0.5-026-j25-26.1.2`.

### 2.0.5-025-j25-26.1.2

Commit message:

`Register AntiFire admin command with Paper BasicCommand API`

Changes in this update:

- Switched `/_antifire` from legacy `plugin.yml` command wiring to Paper's `registerCommand(...)` API in `onEnable()`.
- Kept the same `onembantifire.admin` permission requirement while using Paper's command visibility and suggestion flow.
- Added a startup log line confirming that `/_antifire` was registered.
- Bumped the release to `2.0.5-025-j25-26.1.2`.

### 2.0.5-024-j25-26.1.2

Commit message:

`Add configurable fire control, admin command, and Gradle wrapper for Paper 26.1.2+`

Changes in this update:

- Moved the plugin to a cleaner structure with separate plugin, listener, settings, and command classes.
- Changed fire-event handling from `MONITOR` style behavior to `HIGHEST` priority cancellation so the plugin can act as
  an actual protection layer instead of only observing.
- Added `BlockSpreadEvent` handling as a second guard against fire spread.
- Added lightweight tracked-fire extinguishing so temporary fire can show briefly and then clear without using a heavy
  world scan.
- Added a configurable `config.yml` with toggles for spread prevention, burn prevention, extinguish timing, and which
  ignition sources should be auto-cleaned.
- Added `/_antifire` admin commands for status, config reload, and in-game setting changes.
- Added the `onembantifire.admin` permission node with `default: false`, so operators do not get command access unless
  explicitly granted.
- Added a startup status log so it is easier to confirm that the plugin loaded early and with the expected settings.
- Added a Gradle Wrapper so builders can run `./gradlew build` from a fresh clone.
- Bumped the release to `2.0.5-024-j25-26.1.2`.

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

`./gradlew build`

The build does not use the local `servers/` folder. The jar is written to:

`build/libs/1MB-AntiFire-v2.0.5-030-j25-26.1.2.jar`

## Commands

- `/_antifire` shows the admin command summary.
- `/_antifire help` shows the admin command summary.
- `/_antifire debug` shows the current config state and requires `onembantifire.admin` for players.
- `/_antifire reload` reloads the config from disk.
- `/_antifire toggle <key> <value>` updates a config key in game and saves it.

All `/_antifire` admin commands require the `onembantifire.admin` permission node for players, are not granted to operators by default, and remain available from console.

## Version

[Tested build](https://github.com/mrfdev/AntiFire/releases) Version `2.0.5-030-j25-26.1.2`, targeting Paper
1.21.11 and Paper 26.1.2. Last updated: April 2026.
