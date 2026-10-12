package batterysnap.samplewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.xhelas.batterysnap.WidgetProviderContract;
import com.xhelas.batterysnap.WidgetProviderContract.Columns;
import com.xhelas.batterysnap.WidgetProviderContract.State;

/**
 * Home screen widget showing the battery level, remaining time and charge state computed by BatterySnap.
 * <p>
 * The data is read from BatterySnap's content provider (see {@link WidgetProviderContract}). The widget is redrawn when:
 * <ul>
 * <li>BatterySnap broadcasts {@link #ACTION_WIDGET_DATA_UPDATE} because its data changed,</li>
 * <li>the system sends {@link AppWidgetManager#ACTION_APPWIDGET_UPDATE} (widget added, or every 30 minutes),</li>
 * <li>the user taps the widget.</li>
 * </ul>
 */
public class BatterySnapWidgetProvider extends AppWidgetProvider {

    private static final String tag = BatterySnapWidgetProvider.class.getSimpleName();

    /** Sent by BatterySnap when the widget data changed. */
    static final String ACTION_WIDGET_DATA_UPDATE = "com.xhelas.batterysnap.WIDGET_DATA_UPDATE";

    private static final String[] PROJECTION = {Columns.LEVEL, Columns.REMAINING_DURATION, Columns.STATE};

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, "Received intent " + intent);
        }
        if (ACTION_WIDGET_DATA_UPDATE.equals(intent.getAction())) {
            updateAllWidgets(context);
        } else {
            // Dispatches APPWIDGET_UPDATE to onUpdate
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        appWidgetManager.updateAppWidget(appWidgetIds, buildViews(context, queryBatterySnap(context)));
    }

    private static void updateAllWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BatterySnapWidgetProvider.class));
        if (appWidgetIds.length > 0) {
            appWidgetManager.updateAppWidget(appWidgetIds, buildViews(context, queryBatterySnap(context)));
        }
    }

    /**
     * Reads the current values from BatterySnap.
     *
     * @return the values, or {@code null} if BatterySnap is not installed or returned nothing.
     */
    private static BatteryData queryBatterySnap(Context context) {
        try (Cursor cursor = context.getContentResolver().query(WidgetProviderContract.CONTENT_URI, PROJECTION, null, null, null)) {
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }
            BatteryData data = new BatteryData(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Columns.LEVEL)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Columns.REMAINING_DURATION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Columns.STATE)));
            if (BuildConfig.DEBUG) {
                Log.d(tag, "Queried " + data);
            }
            return data;
        } catch (RuntimeException e) {
            // SecurityException or IllegalArgumentException if the provider is missing or changed
            Log.w(tag, "Cannot query BatterySnap", e);
            return null;
        }
    }

    private static RemoteViews buildViews(Context context, BatteryData data) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_snap_widget);
        if (data == null) {
            views.setTextViewText(R.id.WidgetLevel, context.getString(R.string.level_unknown));
            views.setTextViewText(R.id.WidgetRemaining, "");
            views.setTextViewText(R.id.WidgetState, context.getString(R.string.no_battery_snap));
        } else {
            views.setTextViewText(R.id.WidgetLevel, context.getString(R.string.level_format, data.level));
            views.setTextViewText(R.id.WidgetRemaining, formatRemaining(context, data.remainingMillis));
            views.setTextViewText(R.id.WidgetState, context.getString(stateLabel(data.state)));
        }

        // Tapping the widget asks for a refresh
        Intent refresh = new Intent(context, BatterySnapWidgetProvider.class).setAction(ACTION_WIDGET_DATA_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, refresh,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.BatterySnapWidget, pendingIntent);
        return views;
    }

    private static String formatRemaining(Context context, long remainingMillis) {
        if (remainingMillis <= 0) {
            // BatterySnap has no estimate yet
            return "";
        }
        long minutes = remainingMillis / 60_000;
        return context.getString(R.string.remaining_format, (int) (minutes / 60), (int) (minutes % 60));
    }

    private static int stateLabel(int state) {
        switch (state) {
            case State.FULL:
                return R.string.state_full;
            case State.RELOADING_USB:
                return R.string.state_charging_usb;
            case State.RELOADING_AC:
                return R.string.state_charging_ac;
            case State.RELOADING_WIRELESS:
                return R.string.state_charging_wireless;
            case State.DISCHARGING:
            default:
                return R.string.state_discharging;
        }
    }

    private static final class BatteryData {
        final int level;
        final long remainingMillis;
        final int state;

        BatteryData(int level, long remainingMillis, int state) {
            this.level = level;
            this.remainingMillis = remainingMillis;
            this.state = state;
        }

        @Override
        public String toString() {
            return "level=" + level + " remainingMillis=" + remainingMillis + " state=" + state;
        }
    }
}
