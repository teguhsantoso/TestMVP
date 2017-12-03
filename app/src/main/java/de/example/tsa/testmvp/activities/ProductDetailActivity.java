package de.example.tsa.testmvp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.example.tsa.testmvp.R;
import de.example.tsa.testmvp.entities.Product;
import de.example.tsa.testmvp.presenters.DetailProductPresenter;
import de.example.tsa.testmvp.presenters.DetailProductPresenterCallback;
import de.example.tsa.testmvp.presenters.DetailProductPresenterImpl;
import de.example.tsa.testmvp.services.Constants;

public class ProductDetailActivity extends AppCompatActivity implements DetailProductPresenterCallback {
    private TextView                textViewProductId;
    private TextView                textViewBarcodeId;
    private EditText                editTextSumOders;
    private EditText                editTextDescription;
    private TextView                textViewTimestamp;
    private Product                 selectedProduct;
    private DetailProductPresenter  presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        this.selectedProduct = (Product)getIntent().getSerializableExtra(Constants.INTENT_SELECTED_PRODUCT);
        this.textViewProductId = findViewById(R.id.textViewProductId);
        this.textViewBarcodeId = findViewById(R.id.textViewBarcodeId);
        this.editTextSumOders = findViewById(R.id.editTextSumOrder);
        this.editTextDescription = findViewById(R.id.editTextDescription);
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
        initProductData();
    }

    private void initProductData() {
        this.textViewProductId.setText("Product-Id. : " + selectedProduct.getItemId() + " - " + selectedProduct.getName());
        this.textViewBarcodeId.setText("Barcode-Id. : " + selectedProduct.getBarcodeId());
        this.editTextSumOders.setText(String.valueOf(selectedProduct.getSumOfOrders()));
        this.editTextDescription.setText(selectedProduct.getDescription());
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

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goBack() {
        finish();
    }

    public void cancelAction(View view){
        finish();
    }

    public void confirmAction(View view){
        this.selectedProduct.setSumOfOrders(Integer.valueOf(this.editTextSumOders.getText().toString().trim()));
        this.selectedProduct.setDescription(this.editTextDescription.getText().toString());
        this.presenter.updateProductData(this.selectedProduct);
    }

}
