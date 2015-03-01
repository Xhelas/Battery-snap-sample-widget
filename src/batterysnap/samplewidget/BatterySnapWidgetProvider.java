package batterysnap.samplewidget;

import android.appwidget.AppWidgetProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.xhelas.batterysnap.WidgetProviderContract;
import com.xhelas.batterysnap.WidgetProviderContract.Columns;

import java.util.Arrays;

public class BatterySnapWidgetProvider extends AppWidgetProvider {

  private static final String tag = AppWidgetProvider.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
    if (BuildConfig.DEBUG) {
      Log.d(tag, "Received intent " + intent);
    }
    Uri uri = WidgetProviderContract.CONTENT_URI;
    ContentResolver contentResolver = context.getContentResolver();
    if (BuildConfig.DEBUG) {
      Log.d(tag, "Querying with uri " + uri);
    }
    Cursor cursor = null;
    try {
      cursor = contentResolver.query(uri, new String[]{Columns.REMAINING_DURATION, Columns.LEVEL}, null, null, null);
    } catch (Exception e) {
      if (BuildConfig.DEBUG) {
        Log.e(tag, "Exception in query", e);
      }
    }

    long duration = -1;
    int level = -1;
    if (cursor != null) {
      if (BuildConfig.DEBUG) {
        Log.d(tag, "Column names are " + Arrays.toString(cursor.getColumnNames()));
      }
      cursor.moveToFirst();
      duration = cursor.getLong(cursor.getColumnIndex(Columns.REMAINING_DURATION));
      level = cursor.getInt(cursor.getColumnIndex(Columns.LEVEL));
      cursor.close();
    }
    if (BuildConfig.DEBUG) {
      Log.d(tag, "Duration is " + duration + " and level is " + level);
    }
  }

  @Override
  public void onDisabled(Context context) {
    if (BuildConfig.DEBUG) {
      Log.d(tag, "Service is stopped");
    }
    context.stopService(new Intent(context, WidgetService.class));
  }
}
