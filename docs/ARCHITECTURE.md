# Architecture

Skyburst Nocturne is a small Android application with no runtime dependencies. `FireworksDreamService` hosts the full-screen Android dream. `FireworksSceneView` draws an approximately 30-frame-per-second scene with Android Canvas: reusable gradient shaders, a low city skyline, twinkling stars, rising launch trails, and expanding spark particles that fall under gravity and fade. Skyline geometry and window positions are prepared when the surface changes. Bursts have a fixed maximum count and each uses a bounded particle array.

The launcher settings screen persists D-pad-accessible choices with Android SharedPreferences. `FireworksOptions` validates selections and resolves per-setting Random and Randomize All when the view is created. `SettingsProvider` publishes a small, versioned interface for host apps and rejects values outside the declared schema.

The renderer uses no full-screen cached bitmap, video decoder, background service, wake lock, network client, bundled media or third-party runtime libraries. The DreamService stops animation on pause/detach and releases its shader and skyline references. Particle and star counts are bounded. Frame pacing and sustained TV thermal behavior still need physical-device measurements.
