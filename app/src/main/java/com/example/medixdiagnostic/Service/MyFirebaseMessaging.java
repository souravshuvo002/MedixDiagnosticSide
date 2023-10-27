package com.example.medixdiagnostic.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.medixdiagnostic.Activity.BookHistoryActivity;
import com.example.medixdiagnostic.Api.ApiService;
import com.example.medixdiagnostic.Api.ApiURL;
import com.example.medixdiagnostic.Common.Common;
import com.example.medixdiagnostic.Helper.NotificationHelper;
import com.example.medixdiagnostic.Model.Result;
import com.example.medixdiagnostic.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if (Common.currentDiagnostic != null) {
            updateTokenRefresh(s);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData() != null)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                sendNotificationAPI26(remoteMessage);
            }
            else
            {
                sendNotification(remoteMessage);
            }
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        // Get Information From Message
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        Intent intent = new Intent(this, BookHistoryActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), builder.build());

    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {
        // Get Information From Message
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        // From API level 26, we need implement Notification Channel
        NotificationHelper helper;
        Notification.Builder builder;

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        helper = new NotificationHelper(this);
        builder = helper.getNotification(title,message,defaultSoundUri);
        helper.getManager().notify(new Random().nextInt(), builder.build());
    }

    private void updateTokenRefresh(String token) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String DIAGNOSTIC_ID = sharedPreferences.getString("DIAGNOSTIC_ID", null);

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiURL.MEDIX_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiService service = retrofit.create(ApiService.class);

        //defining the call
        Log.d("Token: ", token);
        Call<Result> call = service.updateDiagnosticToken(DIAGNOSTIC_ID, token);
        //calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("FS_Debug: ", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("FS_Debug: ", t.getMessage());
            }
        });
    }
}
