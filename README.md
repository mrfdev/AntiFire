# 1MB AntiFire

**This is a Helper plugin for the fire-damage events we want to have better control over on the 1MoreBlock.com Minecraft
Java server, targeting PaperMC 26.1.2 with compatibility testing on 26.2.**

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
The current build compiles against the Paper API `26.1.2` line, declares `api-version: 26.1.2` in `plugin.yml`, and
uses a Java 25 toolchain. The supported live target is Paper `26.1.2`, while Paper `26.2` is treated as a compatibility
smoke-test target instead of a separate build target. Production servers can run newer Java runtimes such as Java
`26.0.1`, but this plugin intentionally compiles for Java `25` because that is the Paper/Mojang baseline for the `26.x`
generation.

The goal for this stage is to keep the plugin small, dependable, and easy to maintain while preserving its original
purpose: stop unwanted fire spread, prevent block burn damage, and let controlled fire on netherrack continue to work
as expected. The plugin can also optionally allow soul fire on soul sand and soul soil to remain lit without a restart.
Future changes should continue to prioritize compatibility with newer PaperMC releases in the `26.1.2+` range.

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

### 2.0.5-035-j25-26.1.2

Commit message:

`Style antifire admin output and focus support on Paper 26.1.2+`

Changes in this update:

- Reworked `/_antifire` help, debug, reload, error, and toggle responses to use MiniMessage with the soft pastel 1MB-style presentation used in the other plugin projects.
- Added the new `allow-permanent-soul-fire` config key so soul fire on soul sand and soul soil can be allowed live without a restart.
- Added the live admin toggle example `/_antifire toggle allow-permanent-soul-fire true` to command output and docs.
- Dropped the old Paper `1.21.11` support target from current metadata and docs, and changed `plugin.yml` to declare `api-version: 26.1.2`.
- Kept the compile target on Paper API `26.1.2` while treating Paper `26.2` as compatibility coverage.
- Synced the main README with the current runtime target, command list, command examples, permissions, toggle keys, and placeholder support notes.
- Bumped the release to `2.0.5-035-j25-26.1.2`.

### 2.0.5-034-j25-26.1.2

Commit message:

`Preserve config comments across create reload and command edits`

Changes in this update:

- Replaced the default `saveDefaultConfig()` plus `copyDefaults(true)` flow with a shared YAML config helper that loads with `loadFromString(...)`, keeps comment parsing enabled, and saves through `YamlConfiguration`.
- Added clear per-setting comments for every `config.yml` key, including default values, safe value notes, and reload behavior.
- Preserved admin-edited values while safely adding missing defaults and only filling in comments when they are missing.
- Kept config file work synchronous because it only happens during lightweight startup, reload, and command-driven config edits.
- Bumped the release to `2.0.5-034-j25-26.1.2`.

### 2.0.5-033-j25-26.1.2

Commit message:

`Enable Gradle configuration cache by default`

Changes in this update:

- Fixed the custom `printBuildConfig` task so it no longer reaches into the Gradle task container at execution time.
- Enabled Gradle configuration cache by default for this project after verifying that `build` works with `--configuration-cache`.
- Kept plugin code and runtime behavior unchanged.
- Bumped the release to `2.0.5-033-j25-26.1.2`.

### 2.0.5-032-j25-26.1.2

Commit message:

`Modernize build metadata handling and enable deprecation lint`

Changes in this update:

- Checked the plugin code against the requested Bukkit/Paper deprecation patterns and found no remaining deprecated plugin metadata or YAML APIs in active use.
- Fixed the Gradle resource-filtering deprecation by replacing execution-time `project` lookups with a plain resource properties map.
- Enabled Java compiler deprecation and removal lint in the Gradle build so future deprecated Paper/Bukkit usage is surfaced earlier.
- Kept the same Paper API compile target (`26.1.2`), plugin.yml compatibility floor (`1.21.11`), Java target (`25`), and plugin behavior.
- Bumped the release to `2.0.5-032-j25-26.1.2`.

### 2.0.5-031-j25-26.1.2

Commit message:

`Target Paper API 26.1.2 and move testing to the centralized runner`

Changes in this update:

- Switched the compile dependency to the Paper API `26.1.2` line while keeping `plugin.yml` at `api-version: 1.21.11`.
- Moved jar output to repo-root `libs/` so released jars survive future clean builds instead of being wiped from `build/libs/`.
- Added build metadata output in startup/debug so it is obvious which Paper API line the jar compiles against, which compatibility floor it declares, and that it targets Java 25.
- Updated the project notes to use `/Users/floris/Projects/Codex/servers/run-test-server` instead of any repo-local `/servers/` setup.
- Bumped the release to `2.0.5-031-j25-26.1.2`.

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

## Runtime And Compatibility

- Compile target: Paper API `26.1.2`
- Declared `plugin.yml` `api-version`: `26.1.2`
- Java compilation target: `25`
- Intended live target: Paper `26.1.2`
- Compatibility smoke-test target: Paper `26.2`
- Expected runtime: Java `25+` with successful local testing on Java `26.0.1`
- Startup behavior: `load: STARTUP`
- Early-load hint: `loadbefore: [ Multiverse-Core, WorldGuard ]`

## Build

Clone the project and run:

`./gradlew build`

The build does not use the local `servers/` folder. The jar is written to:

`libs/1MB-AntiFire-v2.0.5-035-j25-26.1.2.jar`

When you want to test the plugin, use the centralized runner:

`/Users/floris/Projects/Codex/servers/run-test-server --paper 26.1.2 --plugin libs/1MB-AntiFire-v2.0.5-035-j25-26.1.2.jar --foreground`

`/Users/floris/Projects/Codex/servers/run-test-server --paper 26.2 --plugin libs/1MB-AntiFire-v2.0.5-035-j25-26.1.2.jar --foreground`

## Commands

- `/_antifire` shows the admin help summary.
- `/_antifire help` shows the admin help summary.
- `/_antifire debug` shows build metadata, active protection settings, and permanent-fire exceptions.
- `/_antifire reload` reloads `config.yml` from disk and applies it immediately.
- `/_antifire toggle <key> <value>` updates one config key in game and saves it immediately.

## Command Examples

- `/_antifire`
- `/_antifire help`
- `/_antifire debug`
- `/_antifire reload`
- `/_antifire toggle allow-permanent-soul-fire true`
- `/_antifire toggle allow-permanent-soul-fire false`
- `/_antifire toggle extinguish-delay-ticks 60`
- `/_antifire toggle check-interval-ticks 20`
- `/_antifire toggle startup-log false`

## Toggle Keys

- `prevent-fire-spread` stops natural fire spread from igniting nearby blocks.
- `prevent-block-burn` prevents burning blocks from being destroyed by fire.
- `extinguish-enabled` turns delayed cleanup of tracked temporary fire on or off.
- `extinguish-delay-ticks` sets how long tracked temporary fire stays visible before cleanup.
- `check-interval-ticks` sets how often AntiFire checks tracked fire for expiry.
- `track-player-placed-fire` tracks player-placed fire for delayed cleanup.
- `track-lightning-fire` tracks lightning-caused fire for delayed cleanup.
- `track-lava-fire` tracks lava-caused fire for delayed cleanup.
- `track-other-ignite-fire` tracks flint-and-steel and similar ignite causes for delayed cleanup.
- `allow-permanent-soul-fire` lets soul fire on soul sand and soul soil stay lit.
- `startup-log` controls the startup summary logging during plugin enable.

For boolean keys, valid values include `true` and `false`. For tick-based keys, use whole numbers.

## Permissions

- `onembantifire.admin`: required for player use of every `/_antifire` command.
- `default: false`: operators do not receive this automatically.
- Console access is always allowed.
- Grant the permission explicitly through LuckPerms or another permission plugin for trusted admins only.

## Placeholders

This plugin does not currently provide any PlaceholderAPI expansion or custom placeholders.
There are no `%placeholder%` tokens or MiniMessage placeholder hooks to configure at this time.

## Version

[Tested build](https://github.com/mrfdev/AntiFire/releases) Version `2.0.5-035-j25-26.1.2`, targeting Paper
26.1.2 with compatibility coverage on Paper 26.2. Last updated: June 2026.
