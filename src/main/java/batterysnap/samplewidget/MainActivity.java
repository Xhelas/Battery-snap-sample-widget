package batterysnap.samplewidget;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Launcher screen: explains how to add the widget and opens BatterySnap, or its Play Store page when it is not installed.
 */
public class MainActivity extends Activity {

    static final String BATTERY_SNAP_PACKAGE = "com.xelacorp.android.batsnaps";

    private TextView _status;
    private Button _button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _status = findViewById(R.id.Status);
        _button = findViewById(R.id.OpenBatterySnap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // BatterySnap may have been installed or removed while we were in the background
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(BATTERY_SNAP_PACKAGE);
        if (launchIntent != null) {
            _status.setText(R.string.battery_snap_installed);
            _button.setText(R.string.open_battery_snap);
            _button.setOnClickListener(v -> startActivity(launchIntent));
        } else {
            _status.setText(R.string.battery_snap_missing);
            _button.setText(R.string.get_battery_snap);
            _button.setOnClickListener(v -> openPlayStore());
        }
    }

    private void openPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BATTERY_SNAP_PACKAGE)));
        } catch (ActivityNotFoundException e) {
            // No Play Store app: fall back to the browser
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + BATTERY_SNAP_PACKAGE)));
        }
    }
}
