package com.example.uconnect.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uconnect.Interface.ItemClickListner;
import com.example.uconnect.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.product_name_tv);
        txtProductPrice=itemView.findViewById(R.id.product_price_tv);
        txtProductQuantity=itemView.findViewById(R.id.product_quantity_tv);
    }

    @Override
    public void onClick(View v) {

        itemClickListner.onClik(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {

        this.itemClickListner = itemClickListner;
    }
}
