package batterysnap.samplewidget;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

public class ApplicationMain extends Application {
    private static final String tag = ApplicationMain.class.getSimpleName();
    private static ApplicationMain _instance;
    private boolean _keepAlive;

    @Override
    public void onCreate() {
        _instance = this;
        int widgetCount = getWidgetCount();
        if (BuildConfig.DEBUG) {
            Log.d(tag, "Found " + widgetCount + " widgets.");
        }
        if (widgetCount > 0) {
        }
    }

    private int getWidgetCount() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        return appWidgetManager.getAppWidgetIds(new ComponentName(this, BatterySnapWidgetProvider.class)).length;
    }

    static ApplicationMain getInstance(){
        return _instance;
    }

    void startService(){
        startService(new Intent(this, WidgetService.class));
        _keepAlive = true;
        if (BuildConfig.DEBUG) {
            Log.d(tag, "Service is started");
        }
    }
    void stopService(){
        stopService(new Intent(this, WidgetService.class));
        _keepAlive =false;
        if (BuildConfig.DEBUG) {
            Log.d(tag, "Service is stopped");
        }
    }

}
