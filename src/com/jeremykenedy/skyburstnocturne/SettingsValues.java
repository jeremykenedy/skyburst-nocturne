/* Adapted from Perseid Passage under Apache-2.0; modified for Skyburst Nocturne. */
package com.jeremykenedy.skyburstnocturne;

public final class SettingsValues {
    private SettingsValues() {}

    public static boolean isSupported(String key, String value) {
        if (key == null || value == null) return false;
        if ("density".equals(key)) return oneOf(value, "sparse", "balanced", "dense", "packed", "random");
        if ("motion".equals(key)) return oneOf(value, "slow", "natural", "fast", "random");
        if ("rate".equals(key)) return oneOf(value, "quiet", "steady", "festival", "random");
        if ("size".equals(key)) return oneOf(value, "fine", "natural", "grand", "random");
        if ("palette".equals(key)) return oneOf(value, "aurora", "ember", "tropical", "random");
        if ("brightness".equals(key)) return oneOf(value, "night", "dusk", "bright", "random");
        if ("randomize_all".equals(key)) return oneOf(value, "true", "false");
        return false;
    }

    private static boolean oneOf(String value, String... allowed) {
        for (String option : allowed) if (option.equals(value)) return true;
        return false;
    }
}
