#!/usr/bin/env bash
# Adapted from Perseid Passage under Apache-2.0; modified for Skyburst Nocturne.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
python3 -m compileall -q "$ROOT/install.py" "$ROOT/tests" "$ROOT/scripts"
