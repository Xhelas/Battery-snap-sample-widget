package batterysnap.samplewidget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Draws the widget background: two half circles starting at 0° and 180°, growing clockwise.
 * <p>
 * Each half sweeps {@code level}% of 180°, so a full battery draws a full circle, a half full battery two opposite
 * quarter circles, and an empty battery nothing but the faint track.
 */
final class BatteryRing {

    /** Bitmap side in pixels. The ImageView scales it with fitCenter. */
    private static final int SIZE = 256;
    private static final float STROKE = SIZE / 10f;

    private static final int TRACK_COLOR = 0x40FFFFFF;
    private static final int RING_COLOR = 0xFFFFFFFF;

    private BatteryRing() {
    }

    /**
     * @param level battery level in percent, or a negative value when unknown (only the track is drawn).
     */
    static Bitmap draw(int level) {
        Bitmap bitmap = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        float half = STROKE / 2;
        RectF oval = new RectF(half, half, SIZE - half, SIZE - half);

        Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(STROKE);
        stroke.setColor(TRACK_COLOR);
        canvas.drawOval(oval, stroke);

        if (level > 0) {
            float sweep = 180f * Math.min(level, 100) / 100f;
            stroke.setColor(RING_COLOR);
            // Angles are clockwise from 3 o'clock. One half starts at 0° (right), the other at 180° (left), both grow
            // clockwise, and they close the circle when the battery is full.
            canvas.drawArc(oval, 0f, sweep, false, stroke);
            canvas.drawArc(oval, 180f, sweep, false, stroke);
        }
        return bitmap;
    }
}
