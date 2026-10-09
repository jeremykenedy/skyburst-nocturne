# Release process

Skyburst Nocturne uses semantic version tags. A release includes a signed APK, a SHA-256 text file named `skyburst-nocturne.apk.sha256`, and concise notes for the version.

Before publishing:

1. Run `bash test.sh`, `bash build.sh`, and the documented privacy and documentation checks.
2. Verify the application ID, version code/name, DreamService metadata, launcher entry, declared permissions, signer, and APK contents.
3. Install over the previous signed build on the Android TV emulator and verify settings, preview, dream lifecycle, and remote navigation.
4. Capture a new running-app screenshot after the scene settles, inspect the image, and update the verification record.
5. Create a versioned release with upgrade notes and attach the APK and checksum. Download both assets from the release and confirm the hash.

The project key must remain private, outside the repository, and available to future release maintainers. Do not replace the signing certificate for a patch or minor update.
