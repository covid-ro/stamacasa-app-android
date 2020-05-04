package ro.gov.stamacasa.db.evaluationform;

import androidx.annotation.WorkerThread;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ro.gov.stamacasa.data.pojo.forminput.EvaluationFormInput;

@Dao
public interface EvaluationFormDao {

    @WorkerThread
    @Query("SELECT * FROM evaluation_form")
    List<EvaluationFormInput> getAllEvaluations();

    @WorkerThread
    @Query("SELECT * FROM evaluation_form")
    Single<List<EvaluationFormInput>> getAllEvaluationsRx();

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EvaluationFormInput evaluationFormInput);

    @WorkerThread
    @Delete
    void delete(EvaluationFormInput evaluationFormInput);

    @WorkerThread
    @Query("SELECT * FROM evaluation_form WHERE id = :formId")
    EvaluationFormInput getEvaluationFormById(String formId);

    @WorkerThread
    @Query("SELECT * FROM evaluation_form WHERE userId = :uid ORDER BY timestamp  DESC LIMIT :amount")
    Single<List<EvaluationFormInput>> getEvaluationForUser(long uid, int amount);

    @WorkerThread
    @Query("SELECT * FROM evaluation_form WHERE userId = :uid AND timestamp = :timestamp")
    Single<EvaluationFormInput> getEvaluationFormsForUserByStamp(long uid, long timestamp);

    @WorkerThread
    @Query("SELECT timestamp FROM evaluation_form WHERE userId = :uid ORDER BY timestamp DESC LIMIT :amount")
    Single<List<Long>> getEvaluationsTimestampsById(long uid, int amount);

    @WorkerThread
    @Query("SELECT timestamp FROM evaluation_form WHERE userId = :uid ORDER BY timestamp")
    Single<List<Long>> getEvaluationsTimestampsById(long uid);

    @WorkerThread
    @Query("SELECT * FROM evaluation_form WHERE userId = :uid ORDER BY timestamp")
    Single<List<EvaluationFormInput>> getEvaluationsForUser(long uid);
}
