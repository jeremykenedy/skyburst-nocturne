# Device verification

## Emulator validation

The signed release candidate was installed on a Google TV emulator (`sdk_google_atv64_amati_arm64`, Android 14, API 34, 1920 x 1080). The Settings activity opened and displayed all controls. Queries to `content://com.jeremykenedy.skyburstnocturne.settings/schema` and `/settings` returned the documented seven settings and their defaults. The Preview activity rendered the full-screen fireworks scene without text or overlays. Two captures three seconds apart differed in 22,462 pixels, confirming continuous animation.

The main screenshot was captured after allowing the preview to settle, then visually inspected. The settings screenshot shows the complete controls layout. Both are under [`screenshots/`](screenshots/). The app's preview and DreamService share `FireworksSceneView`; the emulator image denied root access, so its root-only `cmd dreams start-dreaming` command could not launch the DreamService directly. The DreamService is declared with `BIND_DREAM_SERVICE`, but its selection, idle activation, stop, and wake lifecycle were not verified on this image. No physical TV was used.

## Untested devices

| Device family | Status | Request |
| --- | --- | --- |
| Amazon Fire TV / Fire OS | No physical device tested | A volunteer with a Fire TV is needed to test installation, screensaver selection, remote settings, idle activation, and wake/resume. Please report model, Fire OS/API version, resolution, behavior tested, and results through the [issue tracker](https://github.com/jeremykenedy/skyburst-nocturne/issues). |
| Android TV | No physical device tested | A volunteer with an Android TV is needed to test selection, remote controls, idle activation, and sustained playback. Please report model, OS/API, resolution, behavior tested, and results through the [issue tracker](https://github.com/jeremykenedy/skyburst-nocturne/issues). |
| Google TV | Emulator only | A volunteer with a Google TV is needed to test the actual screensaver service, idle activation, remote controls, and sustained playback. Please report model, OS/API, resolution, behavior tested, and results through the [issue tracker](https://github.com/jeremykenedy/skyburst-nocturne/issues). |

A 1080p emulator does not establish native 4K output, vendor idle activation, physical-TV remote behavior, long-term thermal behavior, or protection from operating-system setting changes.
