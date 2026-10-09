# Building from source

## Requirements

- JDK 17 or a compatible Java compiler
- Android SDK platform 36
- Android build-tools 36.0.0
- `aapt2`, `d8`, `zipalign`, `apksigner`, `openssl`, `keytool`, and `shasum`

Set `ANDROID_HOME` if the SDK is not under `~/Library/Android/sdk`, then run:

```bash
bash build.sh
```

The build creates `build/skyburst-nocturne.apk` and its checksum. It creates a project-specific signing key and password under `~/.android/` when they do not exist. These files are ignored by Git. Back them up securely: future updates must use the same key. If the keystore is missing while the password file remains, the script stops rather than silently making an incompatible key. Never use a signing key from another screensaver.

To choose a version for a local build:

```bash
VERSION_NAME=1.0.1 VERSION_CODE=2 bash build.sh
```

Use this project key for every release. Keep it private and outside the repository; builds made on another computer will not be upgrade-compatible unless they use the same key.
