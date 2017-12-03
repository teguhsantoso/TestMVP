package de.example.tsa.testmvp.presenters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import de.example.tsa.testmvp.services.Constants;
import de.example.tsa.testmvp.services.TelegramService;

/**
 * Created by Teguh Santoso on 01.12.2017.
 */

public class DetailProductPresenterImpl implements DetailProductPresenter {
    private Context                         cTxt;
    private DetailProductPresenterCallback  presenterCallback;

    public DetailProductPresenterImpl(DetailProductPresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }

    @Override
    public void setContext(Activity activity) {
        this.cTxt = activity;
    }

    @Override
    public void startTelegramService() {
        Intent i = new Intent(cTxt, TelegramService.class);
        i.setAction(Constants.INTENT_ACTION_READ);
        cTxt.startService(i);
        LocalBroadcastManager.getInstance(cTxt).registerReceiver(mMessageReceiverReading, new IntentFilter(Constants.INTENT_ACTION_READ));
    }

    @Override
    public void stopTelegramService() {
        LocalBroadcastManager.getInstance(cTxt).unregisterReceiver(mMessageReceiverReading);
        cTxt.stopService(new Intent(cTxt, TelegramService.class));
    }

    private BroadcastReceiver mMessageReceiverReading = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String response = intent.getStringExtra(Constants.INTENT_EXTRA_RESPONSE);
            presenterCallback.printTimestamp(response);
        }
    };
}
