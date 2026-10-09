# Installation and removal

## Install or update from the latest release

Enable network ADB debugging on the TV, connect it to the same network as your computer, then run:

```bash
python3 install.py --serial TV_IP:5555
```

Review and confirm before installation. The installer contacts this repository's GitHub release API, downloads the exact APK and matching checksum over HTTPS, validates both the release-asset path and final download host, and verifies SHA-256 before ADB install. It does not send device or app usage information. The screensaver app itself has no network permission.

## Install a local APK

```bash
adb -s TV_IP:5555 install -r build/skyburst-nocturne.apk
```

Local installs must be signed with the same certificate as the installed version. The build script preserves a unique local signing key outside the repository for this purpose. Do not use `adb uninstall` before an update because that can discard local settings.

## Activate

Open the TV's system screensaver, ambient display, or dream settings and select **Skyburst Nocturne**. The exact menu names vary by manufacturer and operating system. The app does not silently change the system selection or sleep timers. For Fire TV management, see [Fire TV Toolkit](https://github.com/jeremykenedy/fire-tv-toolkit).

## Remove

```bash
python3 install.py --serial TV_IP:5555 --uninstall
```

The script asks before removal. A non-interactive removal requires the explicit `--uninstall --yes --force` flags. Removing the package also removes its local settings. This installer does not alter the TV's other settings or restore a prior screensaver selection.
