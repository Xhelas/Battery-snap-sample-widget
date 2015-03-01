package batterysnap.samplewidget;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

public class ApplicationMain extends Application {
  private static final String tag = ApplicationMain.class.getSimpleName();

  @Override
  public void onCreate() {
    int widgetCount = getWidgetCount();
    if (BuildConfig.DEBUG) {
      Log.d(tag, "Found " + widgetCount + " widgets.");
    }
    if (widgetCount > 0) {
      startService(new Intent(this, WidgetService.class));
    }
  }

  private int getWidgetCount() {
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
    return appWidgetManager.getAppWidgetIds(new ComponentName(this, BatterySnapWidgetProvider.class)).length;
  }

}
