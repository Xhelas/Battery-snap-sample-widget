package com.xhelas.batterysnap;

import android.net.Uri;
import android.net.Uri.Builder;

public class WidgetProviderContract {

  public static final String AUTHORITY = "com.xhelas.batterysnap.widgetdata";

  public static final Uri CONTENT_URI = new Builder().authority(AUTHORITY).scheme("content").path(null).fragment(null).query(null).build();

  public interface Columns {
    /**
     * The remaining duration in milliseconds. If the battery is loading this will be the remaining duration until full charge.
     * If the battery is discharging, this will be the remaining duration until the battery is empty.
     * <p>Type : LONG
     */
    String REMAINING_DURATION = "remaining_duration";
    /**
     * The level as a percentage of the full charge. Fully charged : 100. Empty : 0.
     * <p>Type : INTEGER
     */
    String LEVEL = "level";
    /**
     * The temperature in deci Celsius. A temperature of 20°C will correspond to a value of 200. This value can be negative.
     * <p>Type : INTEGER
     */
    String TEMPERATURE = "temperature";
    /**
     * The voltage in millivolt.
     * <p>Type : INTEGER
     */
    String VOLTAGE = "voltage";
    /**
     * The state of the battery : see the {@link State} documentation.
     * <p>Type : INTEGER
     */
    String STATE = "state";
  }

  /**
   * Constant describing the states of the battery.
   */
  public interface State {
    /**
     * The battery is full, that is plugged and not loaded.
     */
    public static final int FULL = 0;
    /**
     * The battery is discharging.
     */
    public static final int DISCHARGING = 1;
    /**
     * The battery is plugged by USB and is not full, so it is likely loading.
     */
    public static final int RELOADING_USB = 2;
    /**
     * The battery is plugged by charger and is not full, so it is likely loading.
     */
    public static final int RELOADING_AC = 3;
    /**
     * The battery is in the field of a wireless charger and is not full, so it is likely loading.
     */
    public static final int RELOADING_WIRELESS = 4;
  }
}

