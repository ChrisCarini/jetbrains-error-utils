# jetbrains-error-utils

[![GitHub License](https://img.shields.io/github/license/ChrisCarini/jetbrains-error-utils?style=flat-square)](https://github.com/ChrisCarini/jetbrains-error-utils/blob/master/LICENSE)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/ChrisCarini/jetbrains-error-utils/JetBrains%20Plugin%20CI?logo=GitHub&style=flat-square)](https://github.com/ChrisCarini/jetbrains-error-utils/actions?query=workflow%3A%22JetBrains+Plugin+CI%22)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/ChrisCarini/jetbrains-error-utils/IntelliJ%20Plugin%20Compatibility?label=IntelliJ%20Plugin%20Compatibility&logo=GitHub&style=flat-square)](https://github.com/ChrisCarini/jetbrains-error-utils/actions?query=workflow%3A%22IntelliJ+Plugin+Compatibility%22)

A java library, intended to be consumed by plugins for JetBrains IDEs, providing utilities for handling errors from
plugins.

The library was created to allow me to have shared code across my JetBrains plugins. Ultimately, I hope others also
choose to leverage it, as well as contribute to its development.

## Usage

Below is the instructions for developers wanting to leverage this library in their JetBrains IDE plugins.

### _**TODO:** Add instructions for using the library in a JetBrains IDE plugin gradle project._


## Local Development

Below is the instructions for developers wanting to *develop on* `jetbrains-error-utils` and pull it into a local JetBrains IDE plugin project.

### Snapshotting for local development / use in plugins

1. Make sure the `libraryVersion` in `gradle.properties` is a snapshot version (i.e. it ends in `-SNAPSHOT`;
   e.g. `libraryVersion = 0.0.5-SNAPSHOT`).
2. Run `./gradlew publishToMavenLocal` to publish the snapshot to your local maven repository (e.g. to `~/.m2/repository/com/chriscarini/jetbrains/jetbrains-error-utils/0.0.5-SNAPSHOT/`).
3. In your local plugin project's `build.gradle` file, ensure the following exists:
   ```groovy
   repositories {
      mavenLocal()
   }
   dependencies {
      implementation 'com.chriscarini.jetbrains.error-utils:error-utils:0.0.5-SNAPSHOT'
   }
   ```
4. Rebuild your plugin project.