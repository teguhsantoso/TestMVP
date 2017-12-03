package de.example.tsa.testmvp.presenters;

import android.app.Activity;

import de.example.tsa.testmvp.entities.Product;

/**
 * Created by Teguh Santoso on 01.12.2017.
 */

public interface DetailProductPresenter {

    void setContext(Activity activity);

    void startTelegramService();

    void stopTelegramService();

    void updateProductData(Product selectedProduct);

}
