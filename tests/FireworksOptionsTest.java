/* Adapted from Perseid Passage under Apache-2.0; modified for Skyburst Nocturne. */
package com.jeremykenedy.skyburstnocturne;

import java.util.Random;

public final class FireworksOptionsTest {
    public static void main(String[] args) {
        verifyOptionMapping();
        verifySafeDefaults();
        verifyDistinctChoices();
        verifyRandomSelections();
        verifySettingsValidation();
        System.out.println("Skyburst Nocturne settings tests passed.");
    }

    private static void verifyOptionMapping() {
        FireworksOptions options = FireworksOptions.resolve("packed", "fast", "festival", "grand",
                "tropical", "bright", false, new Random(0));
        check(options.stars == 340, "packed star density");
        check(options.speed == 1.35f, "fast animation speed");
        check(options.rate == 1.05f, "festival launch rate");
        check(options.size == 1.38f, "grand burst size");
        check(options.palette == 2, "tropical palette");
        check(options.brightness == 1f, "bright scene");
    }

    private static void verifySafeDefaults() {
        FireworksOptions defaults = FireworksOptions.resolve("balanced", "natural", "steady", "natural",
                "aurora", "dusk", false, new Random(2));
        check(defaults.stars == 160 && defaults.speed == 1f && defaults.rate == 0.62f
                && defaults.size == 1f && defaults.palette == 0 && defaults.brightness == 0.76f,
                "documented defaults");
        FireworksOptions fallback = FireworksOptions.resolve("invalid", "invalid", "invalid", "invalid",
                "invalid", "invalid", false, new Random(3));
        check(fallback.stars == defaults.stars && fallback.speed == defaults.speed && fallback.rate == defaults.rate
                && fallback.size == defaults.size && fallback.palette == defaults.palette
                && fallback.brightness == defaults.brightness, "invalid choices use defaults");
    }

    private static void verifyDistinctChoices() {
        check(FireworksOptions.resolve("sparse", "slow", "quiet", "fine", "ember", "night",
                false, new Random(1)).stars == 90, "sparse density mapping");
        check(FireworksOptions.resolve("dense", "slow", "quiet", "fine", "ember", "night",
                false, new Random(1)).stars == 220, "dense density mapping");
        check(FireworksOptions.resolve("balanced", "slow", "quiet", "fine", "ember", "night",
                false, new Random(1)).speed == 0.72f, "slow speed mapping");
        check(FireworksOptions.resolve("balanced", "fast", "quiet", "fine", "ember", "night",
                false, new Random(1)).speed == 1.35f, "fast speed mapping");
        check(FireworksOptions.resolve("balanced", "natural", "quiet", "fine", "ember", "night",
                false, new Random(1)).rate == 0.28f, "quiet rate mapping");
        check(FireworksOptions.resolve("balanced", "natural", "festival", "fine", "ember", "night",
                false, new Random(1)).rate == 1.05f, "festival rate mapping");
        check(FireworksOptions.resolve("balanced", "natural", "steady", "fine", "ember", "night",
                false, new Random(1)).size == 0.72f, "fine size mapping");
        check(FireworksOptions.resolve("balanced", "natural", "steady", "grand", "ember", "night",
                false, new Random(1)).size == 1.38f, "grand size mapping");
        check(FireworksOptions.resolve("balanced", "natural", "steady", "natural", "ember", "night",
                false, new Random(1)).palette == 1, "ember palette mapping");
        check(FireworksOptions.resolve("balanced", "natural", "steady", "natural", "tropical", "night",
                false, new Random(1)).palette == 2, "tropical palette mapping");
        check(FireworksOptions.resolve("balanced", "natural", "steady", "natural", "aurora", "night",
                false, new Random(1)).brightness == 0.52f, "night brightness mapping");
        check(FireworksOptions.resolve("balanced", "natural", "steady", "natural", "aurora", "bright",
                false, new Random(1)).brightness == 1f, "bright brightness mapping");
    }

    private static void verifyRandomSelections() {
        FireworksOptions random = FireworksOptions.resolve("random", "random", "random", "random",
                "random", "random", false, new FixedRandom(3));
        check(random.stars == 340 && random.speed == 1.35f && random.rate == 1.05f
                && random.size == 1.38f && random.palette == 2 && random.brightness == 1f,
                "individual Random selections");
        FireworksOptions all = FireworksOptions.resolve("sparse", "slow", "quiet", "fine",
                "aurora", "night", true, new FixedRandom(0));
        check(all.stars == 160 && all.speed == 1f && all.rate == 0.62f && all.size == 1f
                && all.palette == 0 && all.brightness == 0.76f, "Randomize All resolves every setting");
    }

    private static void verifySettingsValidation() {
        check(!SettingsValues.isSupported(null, "night"), "null key rejected");
        check(!SettingsValues.isSupported("density", null), "null value rejected");
        accepts("density", "sparse", "balanced", "dense", "packed", "random");
        accepts("motion", "slow", "natural", "fast", "random");
        accepts("rate", "quiet", "steady", "festival", "random");
        accepts("size", "fine", "natural", "grand", "random");
        accepts("palette", "aurora", "ember", "tropical", "random");
        accepts("brightness", "night", "dusk", "bright", "random");
        check(SettingsValues.isSupported("randomize_all", "true"), "Randomize All enabled");
        check(SettingsValues.isSupported("randomize_all", "false"), "Randomize All disabled");
        check(!SettingsValues.isSupported("rate", "constant"), "unsupported frequency rejected");
        check(!SettingsValues.isSupported("rate", "storm"), "legacy frequency rejected");
        check(!SettingsValues.isSupported("size", "bold"), "legacy size rejected");
        check(!SettingsValues.isSupported("brightness", "day"), "unsupported brightness rejected");
        check(!SettingsValues.isSupported("randomize_all", "yes"), "invalid boolean rejected");
        check(!SettingsValues.isSupported("unknown", "value"), "unknown setting rejected");
    }

    private static void accepts(String key, String... values) {
        for (String value : values) check(SettingsValues.isSupported(key, value), key + " accepts " + value);
    }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }

    private static final class FixedRandom extends Random {
        private final int value;

        FixedRandom(int value) {
            this.value = value;
        }

        @Override
        public int nextInt(int bound) {
            return Math.min(value, bound - 1);
        }
    }
}
