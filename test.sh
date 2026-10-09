#!/usr/bin/env bash
# Adapted from Perseid Passage under Apache-2.0; modified for Skyburst Nocturne.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
OUT="$ROOT/build/tests"
mkdir -p "$OUT"
javac -nowarn -Xlint:-options -source 8 -target 8 -d "$OUT" \
  "$ROOT/src/com/jeremykenedy/skyburstnocturne/FireworksOptions.java" \
  "$ROOT/src/com/jeremykenedy/skyburstnocturne/SettingsValues.java" \
  "$ROOT/tests/FireworksOptionsTest.java"
java -ea -cp "$OUT" com.jeremykenedy.skyburstnocturne.FireworksOptionsTest
python3 -m unittest -v tests.test_installer
python3 "$ROOT/scripts/check-privacy.py"
python3 "$ROOT/scripts/check-docs.py"
bash "$ROOT/scripts/check-style.sh"
