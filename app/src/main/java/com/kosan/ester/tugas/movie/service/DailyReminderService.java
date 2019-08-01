package com.kosan.ester.tugas.movie.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kosan.ester.tugas.movie.R;
import com.kosan.ester.tugas.movie.utils.Contants;
import com.kosan.ester.tugas.movie.utils.Utils;



public class DailyReminderService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String applicationNameTitle = context.getString(R.string.app_name);
        String dailyReminderContent = context.getString(R.string.daily_reminder_content);

        // Create Push Notification
        Utils.createNotification(context,
                applicationNameTitle,dailyReminderContent,
                Contants.NOTIFICATION_DAILY_REMINDER);
    }

}
