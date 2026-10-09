/* Adapted from Perseid Passage under Apache-2.0; modified for Skyburst Nocturne. */
package com.jeremykenedy.skyburstnocturne;

import java.util.Random;

public final class FireworksOptions {
    public final int stars;
    public final float speed;
    public final float rate;
    public final float size;
    public final int palette;
    public final float brightness;

    private FireworksOptions(int stars, float speed, float rate, float size, int palette, float brightness) {
        this.stars = stars;
        this.speed = speed;
        this.rate = rate;
        this.size = size;
        this.palette = palette;
        this.brightness = brightness;
    }

    public static FireworksOptions resolve(String density, String motion, String rate,
            String size, String palette, String brightness, boolean randomizeAll, Random random) {
        return new FireworksOptions(starsFor(choose(density, randomizeAll, random,
                        "balanced", "sparse", "dense", "packed")),
                speedFor(choose(motion, randomizeAll, random, "natural", "slow", "fast")),
                rateFor(choose(rate, randomizeAll, random, "steady", "quiet", "festival")),
                sizeFor(choose(size, randomizeAll, random, "natural", "fine", "grand")),
                paletteFor(choose(palette, randomizeAll, random, "aurora", "ember", "tropical")),
                brightnessFor(choose(brightness, randomizeAll, random, "dusk", "night", "bright")));
    }

    static String choose(String selected, boolean randomizeAll, Random random, String fallback,
            String... values) {
        if (randomizeAll || "random".equals(selected)) {
            int choice = random.nextInt(values.length + 1);
            return choice == 0 ? fallback : values[choice - 1];
        }
        if (fallback.equals(selected)) return fallback;
        for (String value : values) if (value.equals(selected)) return selected;
        return fallback;
    }

    static int starsFor(String density) {
        if ("sparse".equals(density)) return 90;
        if ("dense".equals(density)) return 220;
        if ("packed".equals(density)) return 340;
        return 160;
    }

    static float speedFor(String motion) {
        if ("slow".equals(motion)) return 0.72f;
        if ("fast".equals(motion)) return 1.35f;
        return 1f;
    }

    static float rateFor(String rate) {
        if ("quiet".equals(rate)) return 0.28f;
        if ("festival".equals(rate)) return 1.05f;
        return 0.62f;
    }

    static float sizeFor(String size) {
        if ("fine".equals(size)) return 0.72f;
        if ("grand".equals(size)) return 1.38f;
        return 1f;
    }

    static int paletteFor(String palette) {
        if ("ember".equals(palette)) return 1;
        if ("tropical".equals(palette)) return 2;
        return 0;
    }

    static float brightnessFor(String brightness) {
        if ("night".equals(brightness)) return 0.52f;
        if ("bright".equals(brightness)) return 1f;
        return 0.76f;
    }
}
