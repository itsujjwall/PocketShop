package com.example.uconnect.Seller.ui.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uconnect.Admin.AdminCheckNewProductActivity;
import com.example.uconnect.Model.Products;
import com.example.uconnect.R;
import com.example.uconnect.ViewHolder.ItemViewHolder;
import com.example.uconnect.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference UnverifiedProducsRef;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        UnverifiedProducsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=root.findViewById(R.id.recycler_menu_seller);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(UnverifiedProducsRef.orderByChild("SellerId").equalTo(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()),Products.class).build();

        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull final Products model) {

                        holder.TxtProductName.setText(model.getPname());
                        holder.TxtProductPrice.setText("₹ "+model.getPrice());
                        holder.TxtProductDescription.setText(model.getDescription());
                        holder.TxtProductStatus.setText("Verified : "+model.getVerified());
                        Picasso.get().load(model.getImage()).into(holder.ImageofProduct);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String ProductId=model.getPid();

                                CharSequence[] options =new CharSequence[]{
                                        "Yes",
                                        "No"
                                };
                                final AlertDialog.Builder builder=new AlertDialog.Builder(requireContext(),R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                                builder.setTitle("Do you Want to Delete this Product \n\n" +
                                        "Are you Sure ?\n");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (which==0){

                                            DeleteProduct(ProductId);
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view,parent,false);

                        return new ItemViewHolder(view);
                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void DeleteProduct(final String productId) {

        UnverifiedProducsRef.child(productId).removeValue().addOnCompleteListener
                (new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), productId+" \n Product Deleted Successfully..", Toast.LENGTH_SHORT).show();
//                            if (productId==null){
//                                finish();
//                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error Occur :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
