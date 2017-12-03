package de.example.tsa.testmvp.db;

import android.content.Context;

import java.util.List;

import de.example.tsa.testmvp.entities.Product;

/**
 * Created by Teguh Santoso on 25.11.2017.
 */

public interface RoomInteractor {

    interface OnRoomInteractionListener {
        void onResponse(List products);
        void affectedRow(int rows);
    }

    void initData(Context cTxt, OnRoomInteractionListener listener);

    void findProductsByName(Context cTxt, String name, OnRoomInteractionListener listener);

    void storeData(Context cTxt, Product product, OnRoomInteractionListener listener);

    void deleteProduct(Context cTxt, Product product, OnRoomInteractionListener listener);

    void unsubscribe();

}
