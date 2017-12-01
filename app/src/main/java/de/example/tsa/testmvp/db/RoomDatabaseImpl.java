package de.example.tsa.testmvp.db;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import de.example.tsa.testmvp.entities.Product;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Teguh Santoso on 25.11.2017.
 */

public class RoomDatabaseImpl implements RoomInteractor {
    private AppDatabase                     appDatabase;
    private OnRoomInteractionListener       roomInteractionListener;
    private Subscription                    mProductsSubscription;

    private void createInitDataSingle() {
        Single<List<Product>> initProductsSingle = Single.fromCallable(new Callable<List<Product>>() {

            @Override
            public List<Product> call() throws Exception {
                return populateDatabase();
            }
        });

        mProductsSubscription = initProductsSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Product>>() {
                    @Override
                    public void onSuccess(List<Product> value) {
                        roomInteractionListener.onResponse(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        // Do nothing.
                    }
                });
    }

    private void createFindByNameSingle(String name) {
        Single<List<Product>> initProductsSingle = Single.fromCallable(new Callable<List<Product>>() {

            @Override
            public List<Product> call() throws Exception {
                return findAllByName(name);
            }
        });

        mProductsSubscription = initProductsSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Product>>() {
                    @Override
                    public void onSuccess(List<Product> value) {
                        roomInteractionListener.onResponse(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        // Do nothing.
                    }
                });
    }

    private List<Product> populateDatabase() {
        if(appDatabase.productDao().getAll().isEmpty()){
            appDatabase.productDao().insertProduct(new Product("23009871120", "Netbook Thinkpad10", 5, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 1, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 2, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 3, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 4, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 5, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 6, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 7, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 8, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 9, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 10, "N/A"));
            appDatabase.productDao().insertProduct(new Product("23009871121", "Netbook Toshiba Sattelite Pro", 11, "N/A"));
        }
        return appDatabase.productDao().getAll();
    }

    private List<Product> findAllByName(String name) {
        if(name.trim().isEmpty()){
            return appDatabase.productDao().getAll();
        }
        return appDatabase.productDao().getAllByName("%" + name + "%");
    }

    @Override
    public void initData(Context cTxt, OnRoomInteractionListener listener) {
        this.appDatabase = AppDatabase.getAppDatabase(cTxt);
        this.roomInteractionListener = listener;
        createInitDataSingle();
    }

    @Override
    public void findProductsByName(Context cTxt, String name, OnRoomInteractionListener listener) {
        this.appDatabase = AppDatabase.getAppDatabase(cTxt);
        this.roomInteractionListener = listener;
        createFindByNameSingle(name);
    }

    @Override
    public void storeData(Context cTxt, Product product, OnRoomInteractionListener listener) {
        // TODO
        // Add implementation here.
    }

    @Override
    public void unsubscribe() {
        if (mProductsSubscription != null && !mProductsSubscription.isUnsubscribed()) {
            mProductsSubscription.unsubscribe();
        }
    }

}
