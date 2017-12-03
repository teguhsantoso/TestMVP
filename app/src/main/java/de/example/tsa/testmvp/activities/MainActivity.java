package de.example.tsa.testmvp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import de.example.tsa.testmvp.R;
import de.example.tsa.testmvp.adapters.ProductsItemAdapter;
import de.example.tsa.testmvp.entities.Product;
import de.example.tsa.testmvp.presenters.MainPresenter;
import de.example.tsa.testmvp.presenters.MainPresenterCallback;
import de.example.tsa.testmvp.presenters.MainPresenterImpl;
import de.example.tsa.testmvp.services.Constants;

public class MainActivity extends AppCompatActivity implements MainPresenterCallback, ProductsItemAdapter.OnAdapterInteractionListener {
    private TextView                textViewSumOfProducts;
    private TextView                textViewLoadingData;
    private EditText                editTextSearchName;
    private ProgressBar             progressBar;
    private ProductsItemAdapter     mAdapter;
    private RecyclerView            mRecyclerView;
    private MainPresenter           presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textViewSumOfProducts = findViewById(R.id.textViewSumOfProducts);
        this.textViewLoadingData = findViewById(R.id.textViewLoadingData);
        this.editTextSearchName = findViewById(R.id.editTextSearchName);
        this.progressBar = findViewById(R.id.progressBar);
        this.mRecyclerView = findViewById(R.id.products);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.presenter = new MainPresenterImpl(this);
        this.presenter.setContext(this);
        //this.presenter.initDB();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.presenter.initDB();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.onDestroy();
    }

    @Override
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.textViewLoadingData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.INVISIBLE);
        this.textViewLoadingData.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String s) {
        this.textViewSumOfProducts.setText(s);
    }

    @Override
    public void fillDataProducts(List products) {
        this.mAdapter = new ProductsItemAdapter(this, products);
        this.mRecyclerView.setAdapter(mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void searchProducts(View view){
        this.presenter.findProductsByName(editTextSearchName.getText().toString().trim());
    }

    @Override
    public void onOrderItemClicked(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(Constants.INTENT_SELECTED_PRODUCT, product);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }
}
