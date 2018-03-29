package com.example.kpadmin.hdwallpapers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import com.example.kpadmin.hdwallpapers.Database.DbHelper;

import static com.example.kpadmin.hdwallpapers.Database.CategoryContract.*;
import static com.example.kpadmin.hdwallpapers.Database.CategoryContract.IMAGE_URL_COLUMN;
import static com.example.kpadmin.hdwallpapers.Database.CategoryContract.NOTIFICATION_CONTENT_COLUMN;
import static com.example.kpadmin.hdwallpapers.Database.CategoryContract.NOTIFICATION_EXP_TIME;
import static com.example.kpadmin.hdwallpapers.Database.CategoryContract.NOTIFICATION_TIME_STAMP;
import static com.example.kpadmin.hdwallpapers.Database.CategoryContract.NOTIFICATION_TITLE_COLUMN;
import static com.example.kpadmin.hdwallpapers.Database.CategoryContract.NOTIFICATION_TOTAL_TIME;
import static com.example.kpadmin.hdwallpapers.Database.CategoryContract.TABLE_NOTIFICATION;

/**
 * Created by kpchandora on 22/1/18.
 */

public class NotificationService extends NotificationExtenderService {
    private static final String TAG = "NotificationService";

    @Override
    protected boolean onNotificationProcessing(final OSNotificationReceivedResult notification) {

        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                Log.i("TAG", "extend: " + builder.mContext);

                long currentTime = System.currentTimeMillis();
                long expTime = 5 * 24 * 60 * 60000;

                String url = notification.payload.bigPicture;

                if (url != null) {

                    DbHelper helper = new DbHelper(NotificationService.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(NOTIFICATION_TITLE_COLUMN, builder.mContext.toString());
                    values.put(NOTIFICATION_CONTENT_COLUMN, builder.mContext.toString());
                    values.put(NOTIFICATION_TIME_STAMP, currentTime);
                    values.put(NOTIFICATION_EXP_TIME, expTime);
                    values.put(NOTIFICATION_TOTAL_TIME, currentTime + expTime);
                    values.put(IMAGE_URL_COLUMN, url);

                    long rowId = db.insertWithOnConflict(TABLE_NOTIFICATION, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    Log.i(TAG, "extend:Row " + rowId);
                    Log.i(TAG, "extend: Url " + url);
                } else {
                    Log.i(TAG, "extend: " + url);
                }

                return builder;
            }
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}
