package de.example.tsa.testmvp.presenters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;

import de.example.tsa.testmvp.db.RoomDatabaseImpl;
import de.example.tsa.testmvp.db.RoomInteractor;
import de.example.tsa.testmvp.entities.Product;
import de.example.tsa.testmvp.services.Constants;
import de.example.tsa.testmvp.services.TelegramService;

/**
 * Created by Teguh Santoso on 01.12.2017.
 */

public class DetailProductPresenterImpl implements DetailProductPresenter, RoomInteractor.OnRoomInteractionListener {
    private Context                         cTxt;
    private DetailProductPresenterCallback  presenterCallback;
    private RoomInteractor                  roomInteractor;

    public DetailProductPresenterImpl(DetailProductPresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
        this.roomInteractor = new RoomDatabaseImpl();
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

    @Override
    public void updateProductData(Product selectedProduct) {
        this.roomInteractor.storeData(cTxt, selectedProduct, this);
    }

    private BroadcastReceiver mMessageReceiverReading = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
        String response = intent.getStringExtra(Constants.INTENT_EXTRA_RESPONSE);
        presenterCallback.printTimestamp(response);
        }
    };

    @Override
    public void onResponse(List products) {

    }

    @Override
    public void affectedRow(int rows) {
        presenterCallback.showToast("Update success, row affected: " + rows);
    }
}
