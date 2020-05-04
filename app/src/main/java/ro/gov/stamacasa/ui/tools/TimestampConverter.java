package ro.gov.stamacasa.ui.tools;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimestampConverter {

    public String getMonthAndDay(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M", Locale.getDefault());
        return sdf.format(timestamp);
    }

    public String getDayYearTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
