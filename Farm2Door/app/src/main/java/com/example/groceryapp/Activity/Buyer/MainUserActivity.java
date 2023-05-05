package com.example.groceryapp.Activity.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapp.Activity.LoginActivity;
import com.example.groceryapp.SettingActivity;
import com.example.groceryapp.Adapter.SectionPagerAdapter;
import com.example.groceryapp.Fragment.OrderUserFragment;
import com.example.groceryapp.Fragment.ShopFragment;
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

public class MainUserActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SectionPagerAdapter viewPagerAdapter;

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_user);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        checkUser();

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Shops");
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



    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(ShopFragment.newInstance(),"Shops");
        viewPagerAdapter.addFragment(OrderUserFragment.newInstance(),"Orders");
        viewPagerAdapter.addFragment(UserProfileFragment.newInstance(),"Profile");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void makeMeOffline(){
        progressDialog.setMessage("Checking User.....");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("online","false");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(auth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Update successfully
                        auth.signOut();
                        checkUser();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser() {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainUserActivity.this, LoginActivity.class));
            finish();
        }

    }


}