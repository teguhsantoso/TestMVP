package de.example.tsa.testmvp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import de.example.tsa.testmvp.R;
import de.example.tsa.testmvp.entities.Product;
import de.example.tsa.testmvp.presenters.DetailProductPresenter;
import de.example.tsa.testmvp.presenters.DetailProductPresenterCallback;
import de.example.tsa.testmvp.presenters.DetailProductPresenterImpl;

public class ProductDetailActivity extends AppCompatActivity implements DetailProductPresenterCallback {
    private static final String     INTENT_SELECTED_PRODUCT = "SELECTED PRODUCT";
    private DetailProductPresenter  presenter;
    private TextView                textViewProductId;
    private TextView                textViewTimestamp;
    private Product                 selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        this.selectedProduct = (Product)getIntent().getSerializableExtra(INTENT_SELECTED_PRODUCT);
        this.textViewProductId = findViewById(R.id.textViewProductId);
        this.textViewProductId.setText("Product-Id. : " + selectedProduct.getItemId() + " - " + selectedProduct.getName());
        this.textViewTimestamp = findViewById(R.id.textViewTimestamp);
        this.presenter = new DetailProductPresenterImpl(this);
        this.presenter.setContext(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.presenter.startTelegramService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.presenter.stopTelegramService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void printTimestamp(String response) {
        this.textViewTimestamp.setText("Timestamp: " + response);
    }
}
