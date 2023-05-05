package com.example.groceryapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.groceryapp.Adapter.AdapterProductUser;
import com.example.groceryapp.Adapter.AdapterShop;
import com.example.groceryapp.Model.ModelProduct;
import com.example.groceryapp.Model.ModelShop;
import com.example.groceryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShopFragment extends Fragment {

    private FirebaseAuth auth;
    private RecyclerView shopRecyclerView;

    private ArrayList<ModelShop> shopsList;
    private AdapterShop adapterShop;

    private ProgressDialog progressDialog;
    String[] itemIdArray;

    public ShopFragment() {
        // Required empty public constructor
    }

    String finalId = "";
    String Cat = "";

    private RecyclerView productRv;

    private ArrayList<ModelProduct> productsLst;
    private AdapterProductUser adapterProductUser;


    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        int itemIdArraySize = sharedPreferences.getInt("itemIdArraySize", 0);
//        String[] itemIdArray = new String[itemIdArraySize];
//        for (int i = 0; i < itemIdArraySize; i++) {
//            itemIdArray[i] = sharedPreferences.getString("itemIdArray_" + i, "");
//        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("itemIdArraySize")) {
            int itemIdArraySize = sharedPreferences.getInt("itemIdArraySize", 0);
            itemIdArray = new String[itemIdArraySize];
            for (int i = 0; i < itemIdArraySize; i++) {
                itemIdArray[i] = sharedPreferences.getString("itemIdArray_" + i, "");
            }
            // Do something with the itemIdArray

            ArrayList<String> itemIdList = new ArrayList<>(Arrays.asList(itemIdArray));

            for (String itemId : itemIdList) {
                // Toast.makeText(getContext(), "Item ID: " + itemId, Toast.LENGTH_SHORT).show();
            }


// Count the occurrences of each item ID in the list
            Map<String, Integer> itemCountMap = new HashMap<>();
            for (String itemId : itemIdList) {
                if (itemCountMap.containsKey(itemId)) {
                    int count = itemCountMap.get(itemId);
                    itemCountMap.put(itemId, count + 1);
                } else {
                    itemCountMap.put(itemId, 1);
                }
            }

// Find the maximum occurrence count
            int maxCount = 0;
            List<String> maxCountItems = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : itemCountMap.entrySet()) {
                String itemId = entry.getKey();
                int count = entry.getValue();
                if (count > maxCount) {
                    maxCount = count;
                    maxCountItems.clear();
                    maxCountItems.add(itemId);
                } else if (count == maxCount) {
                    maxCountItems.add(itemId);
                }
            }

            String message = "Maximum occurrence count: " + maxCount + "\nItem IDs with maximum occurrence count:";
            for (String itemId : maxCountItems) {
                message += "\n" + itemId;
                finalId = itemId;

            }
            productRv = v.findViewById(R.id.productRecyclerView);



            //Toast.makeText(getActivity(), "Maximum occurrence count: " + maxCount, Toast.LENGTH_SHORT).show();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            Query query = reference.orderByChild("accountType").equalTo("Seller");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();

                        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Product").child(finalId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.exists()){

                                            Cat = snapshot.child("productCategory").getValue().toString();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                }
            });

            loadProducts();

        } else {
            // The value doesn't exist in SharedPreferences
            v.findViewById(R.id.rec).setVisibility(View.GONE);
        }


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        progressDialog = new ProgressDialog(getContext());

        auth = FirebaseAuth.getInstance();


        shopRecyclerView = view.findViewById(R.id.shopRecyclerView);
        loadMyInfo();
    }

    private void loadShops(String city) {

        shopsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("accountType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //clear list before adding
                        shopsList.clear();
                        for (DataSnapshot ds:  snapshot.getChildren()){
                            ModelShop modelShop = ds.getValue(ModelShop.class);

                            String shopCity = ""+ds.child("city").getValue();

                            //show only user city shops

                            if (shopCity.equals(city)){

                                shopsList.add(modelShop);

                            }

                            //if you display all shops,skip the if statement and add this
                            //shopsList.add(modelShop);


                        }
                        //setup adapter
                        adapterShop = new AdapterShop(getContext(), shopsList);
                        //set adapter to recyclerView
                        shopRecyclerView.setAdapter(adapterShop);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadMyInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String city  = ""+ds.child("city").getValue();
                            loadShops(city);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadProducts() {

        productsLst = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("accountType").equalTo("Seller");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Product").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds: snapshot.getChildren()){

                                if (ds.child("productCategory").getValue().toString().equals(Cat)){
                                    ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                                    productsLst.add(modelProduct);

                                }


                            }
                            adapterProductUser = new AdapterProductUser(getActivity(),productsLst);
                            productRv.setAdapter(adapterProductUser);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });




    }
}