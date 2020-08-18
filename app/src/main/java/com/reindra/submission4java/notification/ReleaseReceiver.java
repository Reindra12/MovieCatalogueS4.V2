package com.reindra.submission4java.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.reindra.submission4java.BuildConfig;
import com.reindra.submission4java.R;
import com.reindra.submission4java.activity.DetailActivity;
import com.reindra.submission4java.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReleaseReceiver extends BroadcastReceiver {
    public static final String EXTRA_TYPE = "type";
    public static int ID_RELEASE = 20;
    public ArrayList<Movie> movies;

    public ReleaseReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final int notification = intent.getIntExtra(EXTRA_TYPE, ID_RELEASE);
        if (notification == ID_RELEASE) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            final String today = day.format(new Date());
            AsyncHttpClient client = new AsyncHttpClient();
            movies = new ArrayList<>();
            String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.API_KEY + "&primary_release_date.gte=" + today + "&primary_release_date.lte=" + today;
            client.get(url, new JsonHttpResponseHandler() {
                @SuppressLint("ResourceType")
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        JSONArray list = response.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject movie = list.getJSONObject(i);
                            Movie items = new Movie();
                            items.setId(movie.getInt("id"));
                            items.setPhoto("https://image.tmdb.org/t/p/w185" + movie.getString("poster_path"));
                            items.setTitle(movie.getString("title"));
                            items.setDate(movie.getString("release_date"));
                            items.setRating(movie.getString("vote_average"));
                            items.setOverview(movie.getString("overview"));
                            items.setCountry(movie.getString("original_language"));
                            movies.add(items);
                            showAlarmNotification(context, context.getResources().getString(R.string.reminder_release), null, notification, items);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }

    public void releaseNotificationOn(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReceiver.class);
        intent.putExtra(EXTRA_TYPE, ID_RELEASE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void releaseNotificationOff(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private PendingIntent getPendingIntent(Context context, int notificationId, Movie item) {
        Intent intent;
        if (notificationId == ID_RELEASE) {
            intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.FLAG_EXTRA, item);
        } else {
            return null;
        }
        return PendingIntent.getActivity(context, notificationId, intent, 0);
    }

    private void showAlarmNotification(Context context, String title, @Nullable String message, int notifId, @Nullable Movie item) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager chanel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        if (movies.size() != 0) {
            for (int i = 0; i < movies.size(); i++) {
                inboxStyle.addLine(movies.get(i).getTitle());
            }
        }
        NotificationCompat.Builder builder;
        if (message == null) {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle(title)
                    .setStyle(inboxStyle)
                    .setColor(ContextCompat.getColor(context, android.R.color.transparent));
        } else {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ContextCompat.getColor(context, android.R.color.transparent));
        }

        PendingIntent pendingIntent = getPendingIntent(context, notifId, item);
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(ID_RELEASE, notification);
        }
    }
}
