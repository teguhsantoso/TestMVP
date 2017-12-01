package de.example.tsa.testmvp.presenters;

import java.util.List;

/**
 * Created by Teguh Santoso on 25.11.2017.
 */

public interface MainPresenterCallback {

    void showProgressBar();

    void hideProgressBar();

    void showMessage(String s);

    void fillDataProducts(List products);

}
