package batterysnap.samplewidget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WidgetService extends Service {

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
