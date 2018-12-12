# Sample Metabase Driver: Sudoku

![screenshot](screenshots/sudoku-driver.png)

All you need you do is drop the driver in your `plugins/` directory. You can grab it [here](https://github.com/metabase/sudoku-driver/releases/download/1.0.0/sudoku.metabase-driver.jar) or build it yourself:

## Building the driver 

### Prereq: Install Metabase locally, compiled for building drivers

```bash
cd /path/to/metabase_source
lein install-for-building-drivers
```

### Build it

```bash
lein clean
DEBUG=1 LEIN_SNAPSHOTS_IN_RELEASE=true lein uberjar
```

### Copy it to your plugins dir and restart Metabase
```bash
mkdir -p /path/to/metabase/plugins/
cp target/uberjar/sudoku.metabase-driver.jar /path/to/metabase/plugins/
jar -jar /path/to/metabase/metabase.jar
```

*or:*

```bash
mkdir -p /path/to/metabase_source/plugins
cp target/uberjar/sudoku.metabase-driver.jar /path/to/metabase_source/plugins/
cd /path/to/metabase_source
lein run
```
