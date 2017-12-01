package de.example.tsa.testmvp.presenters;

import android.app.Activity;

/**
 * Created by Teguh Santoso on 01.12.2017.
 */

public interface DetailProductPresenter {
    void setContext(Activity activity);

    void startTelegramService();

    void stopTelegramService();
}
