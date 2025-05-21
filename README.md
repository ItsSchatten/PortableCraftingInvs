# PortableCraftingInvs

**This is a spigot/bukkit plugin.
It allows a player to open crafting type inventories.
(Ex. Enderchest, Crafting Tables, Furnaces, Brewing Stands)**
</br>_This plugin does support Paper, but does not currently use Paper Plugin functionality when loaded on a Paper server._

This project uses abstraction to support multiple Minecraft versions.
This project will at most support three Minecraft versions but usually supports the two latest versions.

## Gradle Information

PCI uses Gradle version 8.14.

PCI uses a modified fork of [Tagavari's NMS-Remap Gradle Plugin](https://github.com/tagavari/nms-remap).
The fork is located [here](https://github.com/itsschatten/nms-remap-plugin).
This plugin is not distributed anywhere, as such it requires you to build the plugin on your machine.
<br/>**View that project for more information.**

### How to Build

To build PCI, execute the below command.
</br>`./gradlew core:build`

_By default, PCI will be built into `core/build/libs` and is named `PortableCraftingInvs-{version}.jar`_

For development, I recommend using the `-Denv=dev` environment flag,
so it is easier to differentiate between in-development jars from stable jars.
If provided, this flag will also use the `dev-path` from the `gradle.properties` file, so you can quickly export the jar
into a Test server folder or something.
If omitted, it will default to `release`.

### Modify Output Directory

Rename the `default-gradle.properties` file in the `root` directory
and supply a valid path to place the compiled jar into the provided directory.

```properties
# Replace this with your version.
version=yourversionhere
# Replace this with your preferred build path, or leave it empty to use the Gradle default.
# Paths will always use your `user.home` path.
build-path=
# Replaces this with your preferred dev path, or leave it default to use the Gradle default.
dev-path=
```

