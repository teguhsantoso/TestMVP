package de.example.tsa.testmvp.presenters;

/**
 * Created by Teguh Santoso on 01.12.2017.
 */

public interface DetailProductPresenterCallback {
    void printTimestamp(String response);
    void showToast(String message);
    void goBack();
}
