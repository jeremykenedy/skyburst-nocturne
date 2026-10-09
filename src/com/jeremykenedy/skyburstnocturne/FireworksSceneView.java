package com.jeremykenedy.skyburstnocturne;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

final class FireworksSceneView extends View {
    private static final long FRAME_DELAY_MS = 33L;
    private static final int MAX_BURSTS = 5;
    private static final int MAX_PARTICLES = 74;
    private static final int[][] PALETTES = {
            {0xff5ce1e6, 0xffb48cff, 0xffffe68a, 0xfff7fbff},
            {0xffff6038, 0xffffbd55, 0xffff4081, 0xffffe6ad},
            {0xff52f2b2, 0xff42b8ff, 0xffff68ca, 0xfff4ffff}
    };
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();
    private final FireworksOptions options;
    private final Star[] stars;
    private final Burst[] bursts = new Burst[MAX_BURSTS];
    private Shader backgroundShader;
    private Shader horizonGlow;
    private Building[] skyline = new Building[0];
    private float horizon;
    private long startedAt;
    private long lastFrameAt;
    private long nextLaunchAt;
    private boolean running;

    FireworksSceneView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        SharedPreferences values = PreferenceManager.getDefaultSharedPreferences(context);
        options = FireworksOptions.resolve(values.getString("density", "balanced"),
                values.getString("motion", "natural"), values.getString("rate", "steady"),
                values.getString("size", "natural"), values.getString("palette", "aurora"),
                values.getString("brightness", "dusk"), values.getBoolean("randomize_all", false),
                new Random(System.currentTimeMillis()));
        stars = new Star[options.stars];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(random.nextFloat(), random.nextFloat(), 0.5f + random.nextFloat() * 1.5f,
                    random.nextFloat() * 0.75f + 0.25f, random.nextFloat() * 6.28f);
        }
    }

    void start() {
        if (!running) {
            running = true;
            startedAt = SystemClock.uptimeMillis();
            lastFrameAt = startedAt;
            nextLaunchAt = startedAt + 700L;
            postInvalidateOnAnimation();
        }
    }

    void stop() {
        running = false;
        removeCallbacks(invalidator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() == 0 || getHeight() == 0) return;
        long now = SystemClock.uptimeMillis();
        float time = (now - startedAt) / 1000f;
        float delta = Math.min(0.06f, Math.max(0f, (now - lastFrameAt) / 1000f));
        lastFrameAt = now;
        canvas.drawRect(0, 0, getWidth(), getHeight(), backdropPaint());
        drawHorizonGlow(canvas);
        drawSkyline(canvas);
        drawStars(canvas, time);
        advanceBursts(now, delta);
        drawBursts(canvas);
        if (running) postDelayed(invalidator, FRAME_DELAY_MS);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (width <= 0 || height <= 0) return;
        int top = options.palette == 1 ? 0xff170b10 : options.palette == 2 ? 0xff08171b : 0xff080d1c;
        int bottom = options.palette == 1 ? 0xff080608 : options.palette == 2 ? 0xff05090f : 0xff03050b;
        backgroundShader = new LinearGradient(0, 0, width * 0.35f, height,
                top, bottom, Shader.TileMode.CLAMP);
        int glow = options.palette == 1 ? 0xffb83b22 : 0xff24365d;
        int center = Color.argb(36, Color.red(glow), Color.green(glow), Color.blue(glow));
        int edge = Color.argb(0, Color.red(glow), Color.green(glow), Color.blue(glow));
        horizon = height * 0.83f;
        horizonGlow = new RadialGradient(width * 0.48f, height * 0.82f, width * 0.62f,
                center, edge, Shader.TileMode.CLAMP);
        createSkyline(width, height);
    }

    private Paint backdropPaint() {
        paint.setShader(backgroundShader);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    private void drawHorizonGlow(Canvas canvas) {
        paint.setShader(horizonGlow);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setShader(null);
    }

    private void createSkyline(int width, int height) {
        ArrayList<Building> buildings = new ArrayList<>();
        Random layout = new Random(4219L);
        float x = 0;
        while (x < width) {
            float buildingWidth = 34f + layout.nextFloat() * 58f;
            float buildingHeight = height * (0.035f + layout.nextFloat() * 0.12f);
            float top = horizon - buildingHeight;
            ArrayList<float[]> windows = new ArrayList<>();
            for (float wy = top + 11f; wy < horizon - 5f; wy += 15f) {
                for (float wx = x + 8f; wx < x + buildingWidth - 5f; wx += 13f) {
                    if (layout.nextFloat() > 0.42f) windows.add(new float[] {wx, wy});
                }
            }
            buildings.add(new Building(x, top, x + buildingWidth, windows.toArray(new float[windows.size()][])));
            x += buildingWidth - 1f;
        }
        skyline = buildings.toArray(new Building[buildings.size()]);
    }

    private void drawSkyline(Canvas canvas) {
        paint.setShader(null);
        paint.setColor(0xff080b13);
        canvas.drawRect(0, horizon, getWidth(), getHeight(), paint);
        paint.setColor(0xff0b101a);
        for (Building building : skyline) {
            canvas.drawRect(building.left, building.top, building.right, horizon, paint);
        }
        paint.setColor(0x40ffd982);
        for (Building building : skyline) {
            for (float[] window : building.windows) {
                canvas.drawRect(window[0], window[1], window[0] + 4f, window[1] + 4f, paint);
            }
        }
    }

    private final Runnable invalidator = new Runnable() {
        @Override
        public void run() {
            if (running) invalidate();
        }
    };

    private void drawStars(Canvas canvas, float time) {
        float width = getWidth();
        float height = getHeight();
        paint.setStyle(Paint.Style.FILL);
        for (Star star : stars) {
            float twinkle = 0.56f + 0.44f * (float) Math.sin(time * 0.8f + star.phase);
            paint.setColor(star.warm ? 0xffffe2b8 : 0xffc9ddff);
            paint.setAlpha((int) (star.alpha * twinkle * options.brightness * 158f));
            canvas.drawCircle(star.x * width, star.y * height, star.radius, paint);
        }
        paint.setAlpha(255);
    }

    private void advanceBursts(long now, float delta) {
        for (int i = 0; i < bursts.length; i++) {
            Burst burst = bursts[i];
            if (burst != null) {
                burst.age += delta * options.speed;
                if (burst.age >= burst.launchTime + burst.sparkLife) bursts[i] = null;
            }
        }
        if (now < nextLaunchAt) return;
        int slot = freeBurstSlot();
        if (slot >= 0) bursts[slot] = new Burst(random, options.size, PALETTES[options.palette]);
        long gap = (long) ((950 + random.nextInt(900)) / options.rate);
        nextLaunchAt = now + Math.max(350L, gap);
    }

    private int freeBurstSlot() {
        for (int i = 0; i < bursts.length; i++) if (bursts[i] == null) return i;
        return -1;
    }

    private void drawBursts(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        for (Burst burst : bursts) {
            if (burst == null) continue;
            float launchProgress = Math.min(1f, burst.age / burst.launchTime);
            float centerX = burst.x * width;
            float centerY = burst.launchY * height + (burst.y - burst.launchY) * height * launchProgress;
            if (launchProgress < 1f) {
                float fade = 1f - launchProgress * 0.55f;
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setColor(burst.color);
                paint.setAlpha((int) (175f * fade * options.brightness));
                paint.setStrokeWidth(3f * options.size);
                canvas.drawLine(centerX, centerY + 54f, centerX, centerY, paint);
                paint.setAlpha((int) (245f * fade * options.brightness));
                canvas.drawCircle(centerX, centerY, 3.2f * options.size, paint);
                continue;
            }
            float age = burst.age - burst.launchTime;
            float fade = Math.max(0f, 1f - age / burst.sparkLife);
            for (int i = 0; i < burst.count; i++) {
                Spark spark = burst.sparks[i];
                float trail = Math.min(age, 0.36f);
                float x = centerX + spark.vx * age;
                float y = centerY + spark.vy * age + 56f * age * age;
                float oldX = centerX + spark.vx * Math.max(0f, age - trail);
                float oldY = centerY + spark.vy * Math.max(0f, age - trail)
                        + 56f * Math.max(0f, age - trail) * Math.max(0f, age - trail);
                int alpha = (int) (fade * spark.alpha * options.brightness);
                paint.setColor(spark.color);
                paint.setAlpha(Math.max(0, Math.min(255, alpha / 2)));
                paint.setStrokeWidth((1.2f + spark.radius) * options.size);
                canvas.drawLine(oldX, oldY, x, y, paint);
                paint.setAlpha(Math.max(0, Math.min(255, alpha)));
                canvas.drawCircle(x, y, spark.radius * options.size, paint);
            }
        }
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        backgroundShader = null;
        horizonGlow = null;
        skyline = new Building[0];
        super.onDetachedFromWindow();
    }

    private static final class Building {
        final float left;
        final float top;
        final float right;
        final float[][] windows;

        Building(float left, float top, float right, float[][] windows) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.windows = windows;
        }
    }

    private static final class Star {
        final float x;
        final float y;
        final float radius;
        final float alpha;
        final float phase;
        final boolean warm;

        Star(float x, float y, float radius, float alpha, float phase) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.alpha = alpha;
            this.phase = phase;
            warm = x > 0.82f;
        }
    }

    private static final class Burst {
        final float x;
        final float launchY;
        final float y;
        final float launchTime;
        final float sparkLife;
        final int color;
        final int count;
        final Spark[] sparks = new Spark[MAX_PARTICLES];
        float age;

        Burst(Random random, float size, int[] colors) {
            x = 0.14f + random.nextFloat() * 0.72f;
            launchY = 1.08f;
            y = 0.22f + random.nextFloat() * 0.35f;
            launchTime = (1.1f + random.nextFloat() * 0.75f) / size;
            sparkLife = 2.4f + random.nextFloat() * 0.8f;
            color = colors[random.nextInt(colors.length)];
            count = 48 + random.nextInt(27);
            for (int i = 0; i < count; i++) {
                double angle = Math.PI * 2.0 * i / count + (random.nextFloat() - 0.5f) * 0.11f;
                float velocity = (74f + random.nextFloat() * 104f) * size;
                sparks[i] = new Spark((float) Math.cos(angle) * velocity,
                        (float) Math.sin(angle) * velocity, colors[random.nextInt(colors.length)],
                        0.8f + random.nextFloat() * 1.7f, 155 + random.nextInt(101));
            }
        }
    }

    private static final class Spark {
        final float vx;
        final float vy;
        final int color;
        final float radius;
        final int alpha;

        Spark(float vx, float vy, int color, float radius, int alpha) {
            this.vx = vx;
            this.vy = vy;
            this.color = color;
            this.radius = radius;
            this.alpha = alpha;
        }
    }
}
