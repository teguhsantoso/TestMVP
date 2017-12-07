package de.example.tsa.testmvp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.example.tsa.testmvp.entities.Product;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TelegramService extends Service {
    private Context                     cTxt;
    private ScheduledExecutorService    scheduler;
    private Subscription                mProductsSubscription;
    private int                         mCounter;

    @Override
    public void onCreate() {
        super.onCreate();
        cTxt = this;
        this.mCounter = 0;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals(Constants.INTENT_ACTION_READ)){
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
        Log.d(Constants.LOGGER, ">>> Start the scheduler.");
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                generateTimestamp();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void generateTimestamp() {
        // Invoke the webservice method.
        getProductDataFromWS();
    }

    private void stopScheduler(){
        Log.d(Constants.LOGGER, ">>> Shut down the scheduler.");
        scheduler.shutdownNow();
    }

    private void getProductDataFromWS() {
        Single<Product> initProductsSingle = Single.fromCallable(new Callable<Product>() {

            @Override
            public Product call() throws Exception {
                mCounter++;
                return new Product("2017" + String.valueOf(mCounter), "Mouse LOGITEC", 5, "Standard USB mouse");
            }
        });

        mProductsSubscription = initProductsSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Product>() {
                    @Override
                    public void onSuccess(Product value) {

                        // Send data timestamp to parent activity.
                        String strTimestamp = AppUtility.getInstance().getCurrentTime(new Date().getTime()) + " - " + value.getBarcodeId();
                        Intent intent = new Intent(Constants.INTENT_ACTION_READ);
                        intent.putExtra(Constants.INTENT_EXTRA_RESPONSE, strTimestamp);
                        LocalBroadcastManager.getInstance(cTxt).sendBroadcast(intent);

                    }

                    @Override
                    public void onError(Throwable error) {
                        // DO nothing.
                    }
                });
    }


}
