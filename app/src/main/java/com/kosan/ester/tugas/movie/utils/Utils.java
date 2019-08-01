package com.kosan.ester.tugas.movie.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kosan.ester.tugas.movie.BuildConfig;
import com.kosan.ester.tugas.movie.R;
import com.kosan.ester.tugas.movie.view.activity.DetailMoviesActivity;
import com.kosan.ester.tugas.movie.view.activity.MainActivity;
import com.kosan.ester.tugas.movie.view.adapter.MoviesAdapter;
import com.kosan.ester.tugas.movie.view.widget.StackRemoteViewsFactory;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;


public class Utils {

    /**
     * The Method Overloading load Image
     *
     * @param targetView used for component Adapter Recycler
     * @param path  used for URL PATH Image Loader
     * @see MoviesAdapter#onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    public static void loadImage(ImageView targetView, String path) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.grey);

        Glide.with(targetView.getContext())
                .load(BuildConfig.IMAGE_LOAD_URL + path)
                .apply(requestOptions)
                .into(targetView);
    }

    /**
     * The Method Overloading load Image used for Widget Adapter
     *
     * @param targetView used for component widget adapter
     * @param path  used for URL PATH Image Loader
     * @see StackRemoteViewsFactory#getViewAt(int)
     */
    public static void loadImage(Context context, RemoteViews targetView, String path) {
        try {
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter();

            Bitmap preview = Glide.with(context)
                    .asBitmap()
                    .load(BuildConfig.IMAGE_LOAD_POSTER_URL + path)
                    .apply(requestOptions)
                    .submit()
                    .get();

            targetView.setImageViewBitmap(R.id.imageView, preview);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * The Method Overloading load Image used for Detail Movie
     *
     * @param targetView used for component Circle Image view
     * @param path  used for URL PATH Image Loader
     * @see DetailMoviesActivity#initView() (int)
     */
    public static void loadImage(CircleImageView targetView, String path) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .override(100, 100);

        Glide.with(targetView.getContext())
                .load(BuildConfig.IMAGE_LOAD_URL + path)
                .apply(requestOptions)
                .into(targetView);
    }

    public static String getMethodName() {
        return new Exception().getStackTrace()[1].getMethodName();
    }


    /**
     *  Change Menu Icon Colour for Vector Drawable
     *
     * @param context used for call Context Compat
     * @param menuItem used for change menu item color.
     * @param color used for change color for rendering vector
     */
    public static void menuIconColor(Context context, MenuItem menuItem, int color) {
        Drawable drawable = menuItem.getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(
                    ContextCompat.getColor(context,
                            color),
                    PorterDuff.Mode.SRC_ATOP);

        }
    }

    /**
     *  The Method Call Create Notification
     *
     * @param context used for another application need context apps.
     * @param title used for title notification.
     * @param description used for description notification.
     * @param notifyId used for Notification ID.
     */
    public static void createNotification(Context context, String title, String description,int notifyId) {

        // Goto Next Activity if user click
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);


        // Init View Builder Notification
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Contants.NOTIFICATION_CHANNEL_GENERAL);
        builder.setSmallIcon(R.drawable.ic_notifications_white);
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setContentIntent(intent);

        NotificationManager notificationManagerCompat = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        /* Suppor OS OREO */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(Contants.NOTIFICATION_CHANNEL_GENERAL,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(Contants.NOTIFICATION_CHANNEL_GENERAL);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifyId, notification);
        }
    }

    /**
     *  The Method is Set Repeating Alarm
     *
     * @param context used for call getSystemService.
     * @param calendar used for set timer calendar.
     * @param pendingIntent used for pending intent.
     */
    public static void setRepeatingAlarm(Context context, Calendar calendar, PendingIntent pendingIntent){
        cancelAlarm(context,pendingIntent);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int sdkValueNow = Build.VERSION.SDK_INT;


        if (sdkValueNow < Build.VERSION_CODES.KITKAT) { //  Check Version < Kitkat
            Objects.requireNonNull(alarmManager).set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (sdkValueNow > Build.VERSION_CODES.KITKAT && sdkValueNow < Build.VERSION_CODES.M) { //  Check Version > Kitkat until OS Marshmallow
            Objects.requireNonNull(alarmManager).setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        } else if (sdkValueNow >= Build.VERSION_CODES.M) { //  Check Version > Marshmallow, Nougat, Oreo
            Objects.requireNonNull(alarmManager).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     *  The Method used for Cancel Alarm
     *
     * @param context used for call getsystemservice.
     * @param pendingIntent used for cancel pending intent for request broadcast receiver
     */
    public static void cancelAlarm(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(alarmManager).cancel(pendingIntent);
    }

    public static <T> PendingIntent getPendingIntent(Context context, Class<T> className, int notificationId){
        Intent alarmIntent = new Intent(context, className);

        return PendingIntent.getBroadcast(context, notificationId, alarmIntent,
                0);
    }

    /**
     *  The Function is set Calender hourse of date, minute and second
     *
     * @param hoursOfDay used for set time of day (hours).
     * @param minute used for set minute of day.
     * @param second used for set second of day.
     * @return get Time Milisecond time.
     */
    public static Calendar getCalender(int hoursOfDay, int minute, int second){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hoursOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar;

    }

    /**
     * The Function Convert Release Date
     *
     * @param value used for check value from date string is null or not
     * @return check value Comming soon or available date release
     */
    public static CharSequence toReleaseDate(String value) {
        return TextUtils.isEmpty(value) ? "Comming Soon" : value;
    }
}

