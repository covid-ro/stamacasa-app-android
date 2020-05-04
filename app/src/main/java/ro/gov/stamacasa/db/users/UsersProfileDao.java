package ro.gov.stamacasa.db.users;

import androidx.annotation.WorkerThread;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import ro.gov.stamacasa.data.pojo.userprofile.UserProfile;

@Dao
public interface UsersProfileDao {

    @WorkerThread
    @Query("SELECT * FROM users_profile WHERE isAdmin = :includeAdmin")
    Single<List<UserProfile>> getAllUsers(boolean includeAdmin);

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserProfile userProfile);

    @WorkerThread
    @Query("UPDATE users_profile SET lastFormDate=:timestamp WHERE id = :id")
    void update(long timestamp, long id);

    @WorkerThread
    @Delete
    void delete(UserProfile userProfile);

    @WorkerThread
    @Query("SELECT * FROM users_profile WHERE id = :userId")
    Single<UserProfile> getUserById(long userId);
}