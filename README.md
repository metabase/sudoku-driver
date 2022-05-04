# Sample Metabase Driver: Sudoku

![screenshot](screenshots/sudoku-driver.png)

All you need you do is drop the driver in your `/path/to/metabase/plugins/` directory.

## Building the driver

## Prereq: Install the Clojure CLI

Make sure you have the `clojure` CLI version `1.10.3.933` or newer installed; you can check this with `clojure
--version`. Follow the instructions at https://clojure.org/guides/getting_started if you need to install a
newer version.

## Build it

```sh
clj -X:build :project-dir "\"$(pwd)\""
```

will create `target/sudoku.metabase-driver.jar`. Copy this file to `/path/to/metabase/plugins/` and restart your
server, and the driver will show up.

## Hacking on the driver locally

It's easiest to create an alias in `~/.clojure/deps.edn` to include the source paths for your driver, e.g.

```clojure
;; ~/.clojure/deps.edn
{:aliases
 {:user/sudoku-driver
  {:extra-deps {metabase/sudoku-driver {:local/root "/home/cam/sudoku-driver"}}
   :jvm-opts   ["-Dmb.dev.additional.driver.manifest.paths=/home/cam/sudoku-driver/resources/metabase-plugin.yaml"]}}}
```

And then start a (n)REPL or run a dev server from the main Metabase project directory with something like:

```sh
# start a regular REPL
clojure -M:user/sudoku-driver

# start an nREPL
clojure -M:user/sudoku-driver:nrepl

# start a local dev server server
clojure -M:user/sudoku-driver:run
```

You can also pass these options directly to `clojure` e.g.

```sh
# start the dev server
clojure \
  -Sdeps '{:deps {metabase/sudoku-driver {:local/root "/home/cam/sudoku-driver"}}}' \
  -J-Dmb.dev.additional.driver.manifest.paths=/home/cam/sudoku-driver/resources/metabase-plugin.yaml \
  -M:run
```
