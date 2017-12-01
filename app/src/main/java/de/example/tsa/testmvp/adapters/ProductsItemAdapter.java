package de.example.tsa.testmvp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.example.tsa.testmvp.R;
import de.example.tsa.testmvp.entities.Product;

public class ProductsItemAdapter extends RecyclerView.Adapter<ProductsItemAdapter.CustomViewHolder> {

    public interface OnAdapterInteractionListener {
        void onOrderItemClicked(Product product);
    }

    private static Context                          cTxt;
    private static List<Product>                    data;
    private static OnAdapterInteractionListener     mListener;

    public ProductsItemAdapter(Context cTxt, List<Product> data) {
        this.cTxt = cTxt;
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_row_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int i) {
        customViewHolder.textViewProductName.setText(data.get(i).getName());
        customViewHolder.textViewSupplierName.setText("Supplier-Id: N/A");
        customViewHolder.textViewSumOrders.setText("Sum orders #" + String.valueOf(data.get(i).getSumOfOrders()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textViewProductName;
        protected TextView textViewSupplierName;
        protected TextView textViewSumOrders;

        public CustomViewHolder(View view) {
            super(view);
            this.textViewProductName = view.findViewById(R.id.textViewProductName);
            this.textViewSupplierName = view.findViewById(R.id.textViewSupplierName);
            this.textViewSumOrders = view.findViewById(R.id.textViewSumOrders);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener = (OnAdapterInteractionListener) cTxt;
                    mListener.onOrderItemClicked(data.get(getAdapterPosition()));
                }
            });
        }
    }
}
