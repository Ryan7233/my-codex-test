#!/usr/bin/env bash
set -euo pipefail

# Copies Unity C# scripts to a local path. Default target matches the path requested.
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SOURCE_DIR="$SCRIPT_DIR/UnityScripts"
TARGET_DIR=${1:-/Users/yanran/PycharmProjects}

if [[ ! -d "$SOURCE_DIR" ]]; then
  echo "Source directory $SOURCE_DIR not found" >&2
  exit 1
fi

mkdir -p "$TARGET_DIR"
cp -v "$SOURCE_DIR"/*.cs "$TARGET_DIR"/

echo "Copied scripts to $TARGET_DIR"
