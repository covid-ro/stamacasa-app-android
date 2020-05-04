package ro.gov.stamacasa.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ro.gov.stamacasa.data.pojo.exit.ExitForm;
import ro.gov.stamacasa.data.pojo.forminput.EvaluationFormInput;
import ro.gov.stamacasa.data.pojo.userprofile.UserProfile;
import ro.gov.stamacasa.db.evaluationform.EvaluationFormDao;
import ro.gov.stamacasa.db.users.UsersExitDao;
import ro.gov.stamacasa.db.users.UsersProfileDao;

@Database(entities = {EvaluationFormInput.class, UserProfile.class, ExitForm.class}, version = 8)
@TypeConverters({Converters.class})
public abstract class FormSectionsDatabase extends RoomDatabase {

    public abstract EvaluationFormDao formDao();
    public abstract UsersProfileDao usersDao();
    public abstract UsersExitDao usersExitDao();

    private static FormSectionsDatabase instance;

    public static synchronized FormSectionsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FormSectionsDatabase.class, "form_sections")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
