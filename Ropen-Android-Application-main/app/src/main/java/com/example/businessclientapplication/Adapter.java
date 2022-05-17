package com.example.businessclientapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import java.util.zip.Inflater;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList<Business> businesses;
    ArrayList<Business> businessesHolder;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");

    public Adapter(ArrayList<Business> businesses) {
        this.businesses = businesses;
        this.businessesHolder = getBusinessesFromFirebase();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView businessName;
        TextView businessLocation;
        TextView businessNumber;
        TextView businessStatus;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            businessName = (TextView) itemView.findViewById(R.id.BusinessName);
            businessLocation = (TextView) itemView.findViewById(R.id.businessLocation);
            businessNumber = (TextView) itemView.findViewById(R.id.businessPhoneNumber);
            businessStatus = (TextView) itemView.findViewById(R.id.businessStatus);
        }
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.business_item_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull Adapter.ViewHolder holder, int position) {
        Business item = businesses.get(position);

        holder.businessNumber.setText(String.valueOf(item.getPhoneNumber()));

        holder.businessName.setText(item.getBusinessName().toUpperCase());
        holder.businessLocation.setText(item.getLocation());

        if (item.opened) {
            holder.businessStatus.setText("Opened");
            holder.businessStatus.setTextColor(Color.GREEN);
        } else {
            holder.businessStatus.setText("Closed");
            holder.businessStatus.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public ArrayList<Business> filter(String charText) {
        charText = charText.toLowerCase();
        ArrayList<Business> holder = new ArrayList<>();

        for (Business business : businessesHolder) {
            if (business.getBusinessName().toLowerCase().contains(charText)) {
                holder.add(business);
            }
        }
        businesses.clear();
        businesses.addAll(holder);

        if (charText.length() == 0) {
            businesses.addAll(businessesHolder);
        }


        notifyDataSetChanged();
        return holder;
    }

    private ArrayList<Business> getBusinessesFromFirebase() {
        ArrayList<Business> mBusiness = new ArrayList<Business>();
        AtomicBoolean userStatus = new AtomicBoolean(false);
        reference.orderByKey().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot result = task.getResult();
                Iterator<DataSnapshot> iterator = result.getChildren().iterator();

                while(iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    Business value = (Business) next.getValue(Business.class);
                    if(value.status == false) {
                        mBusiness.add(value);
                    }
                }
            }
        });
        return mBusiness;
    }

}
