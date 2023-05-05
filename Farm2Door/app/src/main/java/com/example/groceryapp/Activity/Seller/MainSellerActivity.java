package com.example.groceryapp.Activity.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapp.Activity.AddProductActivity;
import com.example.groceryapp.Activity.LoginActivity;
import com.example.groceryapp.Activity.PromotionCodesActivity;
import com.example.groceryapp.SettingActivity;
import com.example.groceryapp.Activity.ShopReviewsActivity;
import com.example.groceryapp.Adapter.SectionPagerAdapter;
import com.example.groceryapp.Fragment.OrdersFragment;
import com.example.groceryapp.Fragment.ProductsFragment;
import com.example.groceryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainSellerActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SectionPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);



        auth = FirebaseAuth.getInstance();

        checkUser();

        init();

        FirebaseUser user = auth.getCurrentUser();

        //setup viewPager and TabLayout
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Products");
        tabLayout.getTabAt(1).setText("Orders");
        tabLayout.getTabAt(2).setText("Profile");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void init() {


    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(ProductsFragment.newInstance(),"Products");
        viewPagerAdapter.addFragment(OrdersFragment.newInstance(),"Orders");
        viewPagerAdapter.addFragment(SellerProfileFragment.newInstance(),"Profile");
        viewPager.setAdapter(viewPagerAdapter);
    }


    private void checkUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainSellerActivity.this, LoginActivity.class));
            fileList();
        }
    }



}