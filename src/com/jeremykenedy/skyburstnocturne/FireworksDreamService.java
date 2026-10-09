/* Adapted from Perseid Passage under Apache-2.0; modified for Skyburst Nocturne. */
package com.jeremykenedy.skyburstnocturne;

import android.service.dreams.DreamService;

public final class FireworksDreamService extends DreamService {
    private FireworksSceneView scene;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
        setScreenBright(false);
        scene = new FireworksSceneView(this);
        setContentView(scene);
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        if (scene != null) scene.start();
    }

    @Override
    public void onDreamingStopped() {
        if (scene != null) scene.stop();
        super.onDreamingStopped();
    }

    @Override
    public void onDetachedFromWindow() {
        if (scene != null) scene.stop();
        scene = null;
        super.onDetachedFromWindow();
    }
}
