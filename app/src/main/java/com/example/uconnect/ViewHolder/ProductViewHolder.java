package com.example.uconnect.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uconnect.Interface.ItemClickListner;
import com.example.uconnect.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener   {

    public TextView TxtProductName,TxtProductDescription,TxtProductPrice;
    public ImageView ImageofProduct;
    public ItemClickListner ClickListner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        ImageofProduct=(ImageView) itemView.findViewById(R.id.product_image);
        TxtProductName=(TextView) itemView.findViewById(R.id.product_name);
        TxtProductDescription=(TextView) itemView.findViewById(R.id.product_description_in_pil);
        TxtProductPrice=(TextView) itemView.findViewById(R.id.product_price);

    }

    public void setItemClickListner(ItemClickListner listner){

        this.ClickListner=listner;
    }

    @Override
    public void onClick(View v) {

        ClickListner.onClik(v,getAdapterPosition(),false);
    }
}
