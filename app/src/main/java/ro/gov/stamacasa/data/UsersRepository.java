package ro.gov.stamacasa.data;

import android.content.Context;

import java.util.List;

import io.reactivex.Single;
import ro.gov.stamacasa.data.pojo.exit.ExitForm;
import ro.gov.stamacasa.data.pojo.userprofile.UserProfile;
import ro.gov.stamacasa.db.FormSectionsDatabase;
import ro.gov.stamacasa.db.users.UsersExitDao;
import ro.gov.stamacasa.db.users.UsersProfileDao;
import ro.gov.stamacasa.tools.SharedPref;

public class UsersRepository {

    private static UsersRepository instance;
    private UsersProfileDao usersProfileDao;
    private UsersExitDao usersExitDao;

    private UsersRepository(Context context) {
        FormSectionsDatabase database = FormSectionsDatabase.getInstance(context.getApplicationContext());
        usersProfileDao = database.usersDao();
        usersExitDao = database.usersExitDao();
    }

    public static synchronized UsersRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UsersRepository(context);
        }
        return instance;
    }

    public Single<List<UserProfile>> getAllUserProfiles(boolean includeAdmin) {
        return usersProfileDao.getAllUsers(includeAdmin);
    }

    public Single<UserProfile> getUserById(long uid) {
        return usersProfileDao.getUserById(uid);
    }

    public void addUserProfile(UserProfile userProfile, Context c) {
        if (!userProfile.isAdmin()) {
            usersProfileDao.insert(userProfile);
        } else {
            long id = usersProfileDao.insert(userProfile);
            SharedPref.getInstance(c.getApplicationContext()).addLong(c.getApplicationContext(), "adminUid", id);
        }
    }

    public long addUserExit(ExitForm nExitForm) {
        return usersExitDao.insert(nExitForm);
    }

    public void updateUserLastForm(long userId, long timestamp){
        usersProfileDao.update(timestamp, userId);
    }

    public Single<List<ExitForm>> getUserExitsById(long userId, int amount){
        return usersExitDao.getExitsByUserId(userId, amount);
    }
}
