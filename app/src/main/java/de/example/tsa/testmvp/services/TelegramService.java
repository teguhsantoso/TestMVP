package de.example.tsa.testmvp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TelegramService extends Service {
    public static final String          INTENT_ACTION_READ = "de.tsa.mvp.READ";
    public static final String          INTENT_EXTRA_RESPONSE = "intent-response";
    private Context                     cTxt;
    private ScheduledExecutorService    scheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        cTxt = this;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals(INTENT_ACTION_READ)){
            startSchedulerReadDataSocket();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScheduler();
    }

    private void startSchedulerReadDataSocket(){
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                generateTimestamp();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void generateTimestamp() {
        String strTimestamp = AppUtility.getInstance().getCurrentTime(new Date().getTime());
        Intent intent = new Intent(INTENT_ACTION_READ);
        intent.putExtra(INTENT_EXTRA_RESPONSE, strTimestamp);
        LocalBroadcastManager.getInstance(cTxt).sendBroadcast(intent);
    }

    private void stopScheduler(){
        scheduler.shutdownNow();
    }

}
