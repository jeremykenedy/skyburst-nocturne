# Testing

Run the option tests, installer tests, privacy and documentation checks, style check, and Android build with:

```bash
bash test.sh
bash build.sh
```

Run Android Lint after the build so it can inspect the compiled classes:

```bash
SDK="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
"$SDK/cmdline-tools/latest/bin/lint" --exitcode --text stdout \
  --resources res --sources src --classpath build/classes \
  --compile-sdk-version 36 --sdk-home "$SDK" --java-language-level 8 .
```

The Java tests exercise every option mapping, safe defaults, invalid-value fallback, random selection and settings-provider validation. Python tests cover installer device selection, prompts, URL and redirect restrictions, release metadata, checksum parsing, size limits, temporary file cleanup, install, and uninstall behavior.

The release candidate was installed on a Google TV emulator (Android 14 API 34, 1920 x 1080). The settings activity, settings schema and values, and animated preview were checked. Pixel comparison of captures three seconds apart confirmed active animation. The production emulator denied root-only DreamService activation, so direct DreamService lifecycle behavior remains unverified. Details and screenshots are in [device verification](VERIFICATION.md). No physical TV was used.
