package com.example.groceryapp.Activity.Buyer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapp.Activity.LoginActivity;
import com.example.groceryapp.Fragment.OrderUserFragment;
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

public class UserProfileFragment extends Fragment {

    private ImageView profileImageView;
    TextView nameTv, userEmailTv, userPhoneTv;
    private FirebaseAuth auth;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        profileImageView =  v.findViewById(R.id.profile_image);
        nameTv = v.findViewById(R.id.userName);
        userEmailTv = v.findViewById(R.id.userEmail);
        userPhoneTv = v.findViewById(R.id.userPhone);
        loadMyInfo();


        v.findViewById(R.id.logout).setOnClickListener(view -> {
            makeMeOffline();
        });

        v.findViewById(R.id.settings).setOnClickListener(view -> {
            startActivity(new Intent(getContext(), SettingActivity.class));
        });

        return v;
    }

    private void loadMyInfo() {
        auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){

                            //get user data
                            String name  = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            String phone = ""+ds.child("phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();


                            nameTv.setText(name + "(" +accountType +")");
                            userEmailTv.setText(email);
                            userPhoneTv.setText(phone);
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

    private void checkUser() {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }

    }

}