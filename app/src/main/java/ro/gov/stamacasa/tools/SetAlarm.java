package ro.gov.stamacasa.tools;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

import ro.gov.stamacasa.broadcast.BroadcastAlarm;

public class SetAlarm {

    private Activity mActivity;

    public SetAlarm(Activity nActivity) {
        mActivity = nActivity;
        set20alarm();
    }

    public SetAlarm set20alarm() {
        setAlarm(20, 0);
        return this;
    }

    public SetAlarm unset20alarm() {
        unsetAlarm(20, 0);
        return this;
    }

    /**
     * Cancel alarm ; Using hour and minute calculate id of alarm
     *
     * @param nHour   - hour at which the alarm should trigger
     * @param nMinute - minute at which the alarm should trigger
     */
    private void unsetAlarm(Integer nHour, Integer nMinute) {
        Intent myIntent = new Intent(mActivity, BroadcastAlarm.class);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mActivity, ((nHour * 100) + nMinute), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mAlarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.cancel(mPendingIntent);

        if (nMinute == 0) {
            Toast.makeText(mActivity, "Anulat alarma " + nHour + ":" + "00", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, "Anulat alarma " + nHour + ":" + nMinute, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set alarm for desired hour and minute; But this is not guarantee because of doze mode... or other android ... things
     *
     * @param nHour   - hour at which the alarm should trigger
     * @param nMinute - minute at which the alarm should trigger
     */
    private void setAlarm(Integer nHour, Integer nMinute) {

        Intent myIntent = new Intent(mActivity, BroadcastAlarm.class);
        myIntent.putExtra("alarm_hour", "" + nHour + ":" + nMinute);
        myIntent.putExtra("request_code", "" + ((nHour * 100) + nMinute));

        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mActivity, ((nHour * 100) + nMinute), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager mAlarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, nHour);
        mCalendar.set(Calendar.MINUTE, nMinute);

        long startUpTime = mCalendar.getTimeInMillis();
        if (System.currentTimeMillis() > startUpTime) {
            startUpTime = startUpTime + 24 * 60 * 60 * 1000;
        }

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_DAY, mPendingIntent);

        if (nMinute == 0) {
            Toast.makeText(mActivity, "Setat alarma " + nHour + ":" + "00", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, "Setat alarma " + nHour + ":" + nMinute, Toast.LENGTH_SHORT).show();
        }
    }
}
