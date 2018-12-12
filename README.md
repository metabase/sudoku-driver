# Sample Metabase Driver: Sudoku

### Prereqs: Install Metabase locally, compiled for building drivers

```bash
cd /path/to/metabase/source
lein install-for-building-drivers
```

### Build it

```bash
lein clean
DEBUG=1 LEIN_SNAPSHOTS_IN_RELEASE=true lein uberjar
```

### Copy it to your plugins dir and restart Metabase
```bash
cp target/uberjar/sudoku.metabase-driver.jar /path/to/metabase/plugins/
jar -jar /path/to/metabase/metabase.jar
```

*or:*

```bash
cp target/uberjar/sudoku.metabase-driver.jar /path/to/metabase/source/plugins
cd /path/to/metabase/source
lein run
```
