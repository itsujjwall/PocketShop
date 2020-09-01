package com.example.uconnect.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uconnect.Admin.AdminModifyProductsActivity;
import com.example.uconnect.Model.Products;
import com.example.uconnect.Prevelent.Prevelent;
import com.example.uconnect.R;
import com.example.uconnect.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference ProductReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        //for getting value from /adminCategoryActivity
       // type=getIntent().getExtras().get("Admin").toString();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            type=getIntent().getExtras().get("Admin").toString();
        }


        //init
        ProductReference= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });





       DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView=navigationView.getHeaderView(0 );
        TextView usernameTv=headerView.findViewById(R.id.user_profile_name);
        CircleImageView ProfileImage=headerView.findViewById(R.id.user_profile_image);


        if (!type.equals("Admin")){
            usernameTv.setText(Prevelent.CurrentOnlineUser.getName());
            Picasso.get().load(Prevelent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(ProfileImage);
        }
    }


//for getting the general user product seen that is uploaded by the admins
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(ProductReference.orderByChild("Verified").equalTo("yes"),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                        holder.TxtProductName.setText(model.getPname());
                        holder.TxtProductPrice.setText("₹ "+model.getPrice());
                        holder.TxtProductDescription.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.ImageofProduct);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (type.equals("Admin")){

                                    Intent intent=new Intent(HomeActivity.this, AdminModifyProductsActivity.class);
                                    intent.putExtra("pid",model.getPid());

                                    startActivity(intent);
                                }
                                else {

                                    Intent intent=new Intent(HomeActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("pid",model.getPid());

                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                        ProductViewHolder holder=new ProductViewHolder(view);

                        return holder;
                    }

                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(@org.jetbrains.annotations.NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_search) {
            if (!type.equals("Admin")){
            Intent intent=new Intent(HomeActivity.this, SearchProductsActivity.class);
            startActivity(intent);
            }
        } else if (id == R.id.nav_categories) {
            if (!type.equals("Admin")){

            }

        } else if (id == R.id.nav_settings) {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_logout) {
            if (!type.equals("Admin")) {
                Paper.book().destroy();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}