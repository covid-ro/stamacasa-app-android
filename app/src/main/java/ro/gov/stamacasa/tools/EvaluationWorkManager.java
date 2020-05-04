package ro.gov.stamacasa.tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.retrofit.RetrofitService;
import ro.gov.stamacasa.retrofit.model.request.UserData;
import ro.gov.stamacasa.retrofit.model.response.form.WsResponseForm;
import ro.gov.stamacasa.ui.intro.SplashScreenActivity;

public class EvaluationWorkManager extends Worker {

    public static final String EVALUATION_MESSAGE = "evaluation_message";
    public static final String USER_DATA = "user_data";
    public static final String EVALUATION_FORM = "evaluation_form";

    public EvaluationWorkManager(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        String userDataString = taskData.getString(USER_DATA);

        UserData userData = new Gson().fromJson(userDataString, UserData.class);
        Call<WsResponseForm> wsResponseForm = RetrofitService.getInstance().getInterface().postFormSimple(userData);
        String responseMessage = "";

        try {
            Response<WsResponseForm> response = wsResponseForm.execute();
            if (response.isSuccessful()) {
                if (response.body() != null && response.body().getData() != null && response.body().getData().getEvaluation() != null && !response.body().getData().getEvaluation().getMessage().isEmpty()) {
                    responseMessage = response.body().getData().getEvaluation().getMessage();
                    showNotification(getApplicationContext().getResources().getString(R.string.evaluation_notification_title), responseMessage);
                }
            } else {
                return Result.retry();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }

        if (!responseMessage.isEmpty()) {
            return Result.success(createOutputData(responseMessage));
        } else {
            return Result.retry();
        }
    }

    private Data createOutputData(String message) {
        return new Data.Builder()
                .putString(EVALUATION_MESSAGE, message)
                .build();
    }

    private void showNotification(String task, String desc) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        PendingIntent i = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (manager != null) {
            String channelId = "evaluation_channel";
            String channelName = "evaluation";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setContentTitle(task)
                    .setContentIntent(i)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(desc));

            manager.notify(1, builder.build());
        }
    }
}
