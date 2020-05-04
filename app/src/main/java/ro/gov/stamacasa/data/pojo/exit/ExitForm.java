package ro.gov.stamacasa.data.pojo.exit;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

@Entity(tableName = "users_exit")
public class ExitForm {
    @PrimaryKey(autoGenerate = true)
    private int mId;
    private long mUserId;
    private String mDate;
    private String mReason;
    private String mStartingHour;
    private String mEndingHour;
    private boolean mWasInDanger;

    @Ignore
    public ExitForm(long nUserId, String nReason, String nStartingHour, String nEndingHour, Boolean nWasInDanger) {

        Calendar mCalendar = Calendar.getInstance();
        NumberFormat mFormat = new DecimalFormat("00");

        mDate = mFormat.format(mCalendar.get(Calendar.DAY_OF_MONTH));
        mDate = mDate.concat("-").concat(mFormat.format(mCalendar.get(Calendar.MONTH)));
        mDate = mDate.concat("-").concat(String.valueOf(mCalendar.get(Calendar.YEAR)));

        this.mReason = nReason;
        this.mUserId = nUserId;
        this.mStartingHour = nStartingHour;
        this.mEndingHour = nEndingHour;
        this.mWasInDanger = nWasInDanger;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public ExitForm() {
    }

    public String getDate() {
        return mDate;
    }

    public int getId() {
        return mId;
    }

    public String getReason() {
        return mReason;
    }

    public String getStartingHour() {
        return mStartingHour;
    }

    public String getEndingHour() {
        return mEndingHour;
    }

    public boolean isWasInDanger() {
        return mWasInDanger;
    }

    public void setDate(String nDate) {
        mDate = nDate;
    }

    public void setId(int nId) {
        mId = nId;
    }

    public void setReason(String nReason) {
        mReason = nReason;
    }

    public void setStartingHour(String nStartingHour) {
        mStartingHour = nStartingHour;
    }

    public void setEndingHour(String nEndingHour) {
        mEndingHour =  nEndingHour;
    }

    public void setWasInDanger(Boolean nWasInDanger) {
        mWasInDanger = nWasInDanger;
    }
}
