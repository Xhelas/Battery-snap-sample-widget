# BatterySnap sample widget

A minimal, open source Android home screen widget that shows how a third-party app can read the battery data computed by **[BatterySnap](https://play.google.com/store/apps/details?id=com.xelacorp.android.batsnaps)**.

BatterySnap watches how your battery charges and discharges and estimates the time remaining until it is full or empty. It shares those values with other apps through a public content provider. This project is the reference client: copy it to build your own widget, watch face, launcher integration, and so on.

- BatterySnap on Google Play: <https://play.google.com/store/apps/details?id=com.xelacorp.android.batsnaps>
- BatterySnap website: <https://xhelas.com/en/battery-snap/>
- Battery statistics per phone model, collected by BatterySnap users: <https://www.battery-snap.com/>

## What the widget shows

- The battery level, in percent
- The remaining time estimated by BatterySnap: until empty when discharging, until full when charging
- The charge state: discharging, USB / AC / wireless charging, full
- The power drawn or received by the battery, in watts, when the device reports its current

Tap the widget to refresh it. If BatterySnap is not installed, the widget says so.

The app also has a small launcher screen with a button that opens BatterySnap, or its Google Play page when it is not installed.

## How it works

1. BatterySnap exposes a read-only `ContentProvider` with authority `com.xhelas.batterysnap.widgetdata`. Its columns, units and state constants are described in [`WidgetProviderContract.java`](src/main/java/com/xhelas/batterysnap/WidgetProviderContract.java). That file is the whole API: copy it into your app unchanged.
2. Whenever its data changes, BatterySnap sends the broadcast `com.xhelas.batterysnap.WIDGET_DATA_UPDATE` to every app that declares a receiver for it.
3. [`BatterySnapWidgetProvider`](src/main/java/batterysnap/samplewidget/BatterySnapWidgetProvider.java) receives that broadcast, queries the provider and redraws the widget.

Your app needs three things in its manifest, all shown in [`AndroidManifest.xml`](src/main/AndroidManifest.xml):

- A `<queries>` entry for the provider authority. Since Android 11, this is required to see BatterySnap's provider.
- A receiver with an intent filter for `com.xhelas.batterysnap.WIDGET_DATA_UPDATE`.
- `android:exported="true"` on that receiver, since the broadcast comes from another app.

### Available columns

| Column | Type | Meaning |
|--------|------|---------|
| `level` | int | Battery level, 0–100 |
| `remaining_duration` | long | Milliseconds until empty (discharging) or full (charging). 0 or negative when there is no estimate yet. |
| `state` | int | `0` full, `1` discharging, `2` USB charging, `3` AC charging, `4` wireless charging |
| `temperature` | int | Tenths of a degree Celsius (`200` = 20 °C) |
| `voltage` | int | Millivolts |
| `power` | int | Milliwatts (current × voltage), signed as the device reports the current: usually positive while charging, negative while discharging. `NULL` when the device reports no usable current. Absent (`getColumnIndex` returns -1) with older BatterySnap versions. |

## Build

Requires JDK 17 or newer and the Android SDK (compile SDK 37).

```bash
./gradlew assembleDebug     # build the APK
./gradlew installDebug      # install on a connected device
./gradlew lint
```

Then install BatterySnap from Google Play, and add the "BatterySnap sample widget" to your home screen.

## License

[Apache License 2.0](LICENSE)
