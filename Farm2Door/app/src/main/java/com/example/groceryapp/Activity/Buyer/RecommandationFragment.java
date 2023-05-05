package com.example.groceryapp.Activity.Buyer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.groceryapp.Activity.ShopDetailsActivity;
import com.example.groceryapp.Adapter.AdapterOrderedItem;
import com.example.groceryapp.Adapter.AdapterProductUser;
import com.example.groceryapp.Model.ModelOrderedItem;
import com.example.groceryapp.Model.ModelProduct;
import com.example.groceryapp.R;
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

public class RecommandationFragment extends Fragment {


    String finalId = "";
    String Cat = "";

    private RecyclerView productRv;

    private ArrayList<ModelProduct> productsLst;
    private AdapterProductUser adapterProductUser;

    public RecommandationFragment() {
        // Required empty public constructor
    }

    public static RecommandationFragment newInstance() {
        RecommandationFragment fragment = new RecommandationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recommandation, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int itemIdArraySize = sharedPreferences.getInt("itemIdArraySize", 0);
        String[] itemIdArray = new String[itemIdArraySize];
        for (int i = 0; i < itemIdArraySize; i++) {
            itemIdArray[i] = sharedPreferences.getString("itemIdArray_" + i, "");
        }

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

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(getActivity(), snapshot.child("productCategory").getValue().toString(), Toast.LENGTH_SHORT).show();
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

        return v;
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
                                    Toast.makeText(getActivity(), "id "+ds.getKey(), Toast.LENGTH_SHORT).show();
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