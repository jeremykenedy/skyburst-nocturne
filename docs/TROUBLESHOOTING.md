# Troubleshooting

## ADB cannot find the TV

Enable ADB debugging, confirm both devices can reach each other, and connect explicitly:

```bash
adb connect TV_IP:5555
adb devices -l
```

Pass the exact authorized serial to `install.py --serial`. When multiple Android devices are connected, the installer requires an explicit serial.

## The app is missing from the screensaver list

Check that installation completed and that the system supports Android DreamService. Open the device's screensaver or ambient display settings. Some manufacturers restrict third-party dreams or use different menu labels.

## The release installer rejects an APK

The installer requires the APK and `.sha256` sibling assets from the same GitHub release. It refuses unexpected release URLs, redirects, invalid checksums, oversized downloads, and APK bytes that do not match the published checksum. Do not bypass these checks; download the release in a browser and verify the published hash if troubleshooting the network path.

## Settings do not appear immediately

Changes are saved on the device and are applied when the next preview or screensaver session starts. Exit the current preview and start it again.

## Uninstall requires confirmation

Interactive removal asks for confirmation. For unattended use, explicitly pass `--uninstall --yes --force`.
