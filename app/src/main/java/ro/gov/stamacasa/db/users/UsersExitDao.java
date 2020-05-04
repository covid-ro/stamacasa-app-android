package ro.gov.stamacasa.db.users;

import androidx.annotation.WorkerThread;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import ro.gov.stamacasa.data.pojo.exit.ExitForm;

@Dao
public interface UsersExitDao {

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ExitForm userProfile);

    @WorkerThread
    @Query("SELECT * FROM users_exit WHERE mUserId = :userId LIMIT :amount")
    Single<List<ExitForm>> getExitsByUserId(long userId, int amount);
}
