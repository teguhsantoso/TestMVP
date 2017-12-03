package de.example.tsa.testmvp.presenters;

import android.app.Activity;

import de.example.tsa.testmvp.entities.Product;

/**
 * Created by Teguh Santoso on 25.11.2017.
 */

public interface MainPresenter {
    void setContext(Activity activity);
    void onDestroy();
    void initDB();
    void findProductsAll();
    void findProductsByName(String name);
    void deleteProduct(Product product);
}
