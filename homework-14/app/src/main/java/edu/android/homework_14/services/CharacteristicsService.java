package edu.android.homework_14.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;

import edu.android.homework_14.MainActivity;
import edu.android.homework_14.R;
import edu.android.homework_14.fragment.CharacteristicsFragment;

public class CharacteristicsService extends Service implements Runnable{
    private NotificationManager notificationManager;
    private boolean stopped = false;
    private Thread thread;
    private boolean keep = false;
    private boolean board = false;
    private boolean bootloader = false;
    private boolean brand = false;
    private boolean device = false;
    private boolean display = false;
    private boolean hardware = false;
    private boolean id = false;
    private boolean serial = false;
    private boolean codename = false;
    private boolean release = false;
    private boolean sdk = false;
    private boolean model = false;
    private int delay = 5;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final boolean check = intent.getBooleanExtra(MainActivity.CHECK_KEY, false);
        boolean restart = intent.getBooleanExtra(CharacteristicsFragment.CHB_RESTART_KEY, false);
        this.keep = intent.getBooleanExtra(CharacteristicsFragment.CHB_KEEP_KEY, false);
        final String delay = intent.getStringExtra(CharacteristicsFragment.EDT_DELAY_KEY);
        this.delay = delay == null ? 5 : Integer.parseInt(delay);

        this.board = intent.getBooleanExtra(CharacteristicsFragment.CHB_BOARD_KEY, false);
        this.bootloader = intent.getBooleanExtra(CharacteristicsFragment.CHB_BOOTLOADER_KEY, false);
        this.brand = intent.getBooleanExtra(CharacteristicsFragment.CHB_BRAND_KEY, false);
        this.device = intent.getBooleanExtra(CharacteristicsFragment.CHB_DEVICE_KEY, false);
        this.display = intent.getBooleanExtra(CharacteristicsFragment.CHB_DISPLAY_KEY, false);
        this.hardware = intent.getBooleanExtra(CharacteristicsFragment.CHB_HARDWARE_KEY, false);
        this.id = intent.getBooleanExtra(CharacteristicsFragment.CHB_ID_KEY, false);
        this.serial = intent.getBooleanExtra(CharacteristicsFragment.CHB_SERIAL_KEY, false);
        this.codename = intent.getBooleanExtra(CharacteristicsFragment.CHB_CODENAME_KEY, false);
        this.release = intent.getBooleanExtra(CharacteristicsFragment.CHB_RELEASE_KEY, false);
        this.sdk = intent.getBooleanExtra(CharacteristicsFragment.CHB_SDK_KEY, false);
        this.model = intent.getBooleanExtra(CharacteristicsFragment.CHB_MODEL_KEY, false);

        if (check) {
            sendCheckNotification();
            stopSelf(startId);
        } else {
            thread = new Thread(this);
            thread.start();
        }
        return restart ? START_STICKY : START_NOT_STICKY;
    }

    private void sendNotification() {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.SHOW_CHARACTERISTICS, MainActivity.CH_TAG);
        //
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        final String content = makeContent();
        // new type
        Notification notification = new Notification.Builder(getBaseContext())
                .setSmallIcon(R.drawable.charakteristics)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Характеристики")
//                .setContentText(Html.fromHtml(content))
                .setStyle(new Notification.BigTextStyle().bigText(content))
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
    }

    private String makeContent() {
        Boolean[] keys = {
                board,                      bootloader,                 brand,
                device,                     display,                    hardware,
                id,                         serial,                     codename,
                release,                    sdk,                        model
        };
        String[] data = {
                Build.BOARD,                Build.BOOTLOADER,           Build.BRAND,
                Build.DEVICE,               Build.DISPLAY,              Build.HARDWARE,
                Build.ID,                   Build.SERIAL,               Build.VERSION.CODENAME,
                Build.VERSION.RELEASE,      Build.VERSION.SDK,          Build.MODEL
        };
        String[] titles = {
                "Build.BOARD",                "Build.BOOTLOADER",           "Build.BRAND",
                "Build.DEVICE",               "Build.DISPLAY",              "Build.HARDWARE",
                "Build.ID",                   "Build.SERIAL",               "Build.VERSION.CODENAME",
                "Build.VERSION.RELEASE",      "Build.VERSION.SDK",          "Build.MODEL"
        };
        StringBuilder builder = new StringBuilder();
        for (int n = 0; n < Math.min(Math.min(keys.length, data.length), titles.length); n++) {
            if (keys[n]) {
                builder.append(titles[n]).append(": ").append(data[n]).append("\r\n");
            }
        }
        return builder.toString().trim();
    }

    private void sendCheckNotification() {
        // new type
        Notification notification = new Notification.Builder(getBaseContext())
                .setSmallIcon(R.drawable.charakteristics)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Характеристики")
                .setContentText("Будут распологаться здесь")
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
    }

    @Override
    public void run() {
        Log.d("MY", "this.keep: " + this.keep);
        do {
            try {
                Thread.sleep(delay * 1000);
                if (!this.stopped) {
                    sendNotification();
                }
            } catch (Exception e) {
                Log.e("MY", "Threat does not want to sleep!", e);
                this.stopped = true;
            }
        } while (!this.stopped && this.keep) ;
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.d("MY", "Almost stopped!");
        if (this.thread != null) {
            this.stopped = true;
            this.thread = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
