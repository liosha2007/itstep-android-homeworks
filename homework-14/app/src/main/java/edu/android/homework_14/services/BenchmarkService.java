package edu.android.homework_14.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import edu.android.homework_14.MainActivity;
import edu.android.homework_14.R;
import edu.android.homework_14.fragment.BenchmarkFragment;
import edu.android.homework_14.fragment.BenchmarkFragment;

public class BenchmarkService extends Service implements Runnable {
    private NotificationManager notificationManager;
    private Thread thread;
    private boolean keep = false;
    private int delay;
    private boolean stopped = false;
    private int counter;
    private int repeat;
    private String storagePath = null;
    private long result = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final boolean check = intent.getBooleanExtra(MainActivity.CHECK_KEY, false);
        boolean restart = intent.getBooleanExtra(BenchmarkFragment.CHB_RESTART_KEY, false);
        this.keep = intent.getBooleanExtra(BenchmarkFragment.CHB_KEEP_KEY, false);
        final String stringCounter = intent.getStringExtra(BenchmarkFragment.EDT_COUNTER_KEY);
        final String stringRepeat = intent.getStringExtra(BenchmarkFragment.EDT_REPEAT_KEY);
        this.counter = stringCounter == null || stringCounter.isEmpty() ? 10000 : Integer.parseInt(stringCounter);
        this.repeat = stringRepeat == null || stringRepeat.isEmpty() ? 5 : Integer.parseInt(stringRepeat);
        final String delay = intent.getStringExtra(BenchmarkFragment.EDT_DELAY_KEY);
        this.delay = delay == null ? 5 : Integer.parseInt(delay);
        this.storagePath = Environment.getExternalStorageDirectory().getPath() + File.separator;

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
        Notification notification = new Notification.Builder(getBaseContext())
                .setSmallIcon(R.drawable.benchmark)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Показатели")
                .setContentText("Результат: " + (this.counter * 1000) / (result * 1024) + " kb/s")
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
    }

    private void sendCheckNotification() {
        // new type
        Notification notification = new Notification.Builder(getBaseContext())
                .setSmallIcon(R.drawable.charakteristics)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Скорость карты памяти")
                .setContentText("Будет показана здесь")
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
    }

    @Override
    public void run() {
        Log.d("MY", "this.storagePath: " + this.storagePath);
        Log.d("MY", "this.counter: " + this.counter);
        do {
            try {
                final String filePath = this.storagePath + File.separator + "benchmark-file.txt";
                FileOutputStream outputStream = new FileOutputStream(filePath);
                try {
                    this.result = 0L;
                    for (int n = 0; n < this.repeat; n++) {
                        final long before = System.currentTimeMillis();
                        for (int m = 0; m < this.counter; m++) {
                            outputStream.write(new byte[1]);
                            outputStream.flush();
                        }
                        final long after = System.currentTimeMillis();
                        result = (result == 0 ? after - before : (result + (after - before)) / 2);
                        Log.d("MY", "this.result: " + this.result);
                    }
                } catch (Exception e) {
                    Log.e("MY", "Can't write file!", e);
                }
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
