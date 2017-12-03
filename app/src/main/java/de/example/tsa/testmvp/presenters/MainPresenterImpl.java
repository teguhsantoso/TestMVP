package de.example.tsa.testmvp.presenters;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import de.example.tsa.testmvp.db.RoomDatabaseImpl;
import de.example.tsa.testmvp.db.RoomInteractor;
import de.example.tsa.testmvp.entities.Product;

/**
 * Created by Teguh Santoso on 25.11.2017.
 */

public class MainPresenterImpl implements MainPresenter, RoomInteractor.OnRoomInteractionListener {
    private Context                 cTxt;
    private MainPresenterCallback   presenterCallback;
    private RoomInteractor          roomInteractor;

    public MainPresenterImpl(MainPresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
        this.roomInteractor = new RoomDatabaseImpl();
    }

    @Override
    public void setContext(Activity activity) {
        this.cTxt = activity;
    }

    @Override
    public void onDestroy() {
        this.presenterCallback = null;
        this.roomInteractor.unsubscribe();
    }

    @Override
    public void initDB() {
        this.presenterCallback.showProgressBar();
        this.roomInteractor.initData(cTxt, this);
    }

    @Override
    public void findProductsByName(String name) {
        this.presenterCallback.showProgressBar();
        this.roomInteractor.findProductsByName(cTxt, name, this);
    }

    @Override
    public void deleteProduct(Product product) {
        this.roomInteractor.deleteProduct(cTxt, product, this);
    }

    @Override
    public void onResponse(List products) {
        this.presenterCallback.hideProgressBar();
        this.presenterCallback.showMessage("Found products size #" + products.size());
        this.presenterCallback.fillDataProducts(products);
    }

    @Override
    public void affectedRow(int rows) {
        this.presenterCallback.showToast("Delete success, row affected: " + rows);
        this.presenterCallback.refreshProductsData();
    }

}
