package de.example.tsa.testmvp.presenters;

import android.app.Activity;

/**
 * Created by Teguh Santoso on 25.11.2017.
 */

public interface MainPresenter {
    void setContext(Activity activity);
    void onDestroy();
    void initDB();
    void findProductsByName(String name);
}
