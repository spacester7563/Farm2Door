package com.example.groceryapp.Activity.Seller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapp.Activity.AddProductActivity;
import com.example.groceryapp.Activity.Buyer.UserProfileFragment;
import com.example.groceryapp.Activity.LoginActivity;
import com.example.groceryapp.Activity.PromotionCodesActivity;
import com.example.groceryapp.Activity.ShopReviewsActivity;
import com.example.groceryapp.R;
import com.example.groceryapp.SettingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class SellerProfileFragment extends Fragment {



    public SellerProfileFragment() {
        // Required empty public constructor
    }

    public static SellerProfileFragment newInstance() {
        SellerProfileFragment fragment = new SellerProfileFragment();
        return fragment;
    }

    private TextView nameTv,shopNameTv,shopEmailTv;
    private ImageView profileImageView;

    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        profileImageView = v.findViewById(R.id.profile_image);
        nameTv = v.findViewById(R.id.sellerName);
        shopNameTv = v.findViewById(R.id.shopName);
        shopEmailTv = v.findViewById(R.id.shopEmail);

        v.findViewById(R.id.logout).setOnClickListener(view -> {
            makeMeOffline();
        });

        v.findViewById(R.id.promoCode).setOnClickListener(view -> {
            startActivity(new Intent(getContext(), PromotionCodesActivity.class));
        });

        v.findViewById(R.id.settings).setOnClickListener(view -> {
            startActivity(new Intent(getContext(), SettingActivity.class));
        });

        v.findViewById(R.id.reviews).setOnClickListener(view -> {
            showShopReview();
        });

        v.findViewById(R.id.editProfile).setOnClickListener(view -> {
            startActivity(new Intent(getContext(), EditSellerAcitivity.class));
        });

        v.findViewById(R.id.addProduct).setOnClickListener(view -> {
            startActivity(new Intent(getContext(), AddProductActivity.class));
        });

        loadMyInfo();

        TextView verificationTv = v.findViewById(R.id.verifyTv);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (!user.isEmailVerified()){
            verificationTv.setVisibility(View.VISIBLE);
            verificationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send verification code
                    FirebaseUser user = auth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Verification Email has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        return v;
    }

    private void loadMyInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            String shopName = ""+ds.child("shopName").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();


                            nameTv.setText(name + "(" +accountType +")");
                            shopNameTv.setText(shopName);
                            shopEmailTv.setText(email);
                            try {
                                Picasso.get().load(profileImage).into(profileImageView);

                            }catch (Exception e)
                            {
                                profileImageView.setImageResource(R.drawable.ic_baseline_person_24);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void makeMeOffline(){
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
                        getActivity().finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showShopReview() {

        Intent intent = new Intent(getContext(), ShopReviewsActivity.class);
        intent.putExtra("shopUid",auth.getUid());
        startActivity(intent);
    }


    private void checkUser() {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }

    }

}