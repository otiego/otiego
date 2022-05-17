package com.example.businessclientapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    ArrayList<Business> businesses;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view);

         AsyncTask<Void, Void, ArrayList> businessData = new AsyncTask<Void, Void, ArrayList>() {
            @Override
            protected ArrayList doInBackground(Void... voids) {
                return getBusinessesFromFirebase();
            }
        }.execute();

        try {
            businesses = businessData.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) findViewById(R.id.business_item_recycler);
        searchView = (SearchView) findViewById(R.id.searchItem);

        Adapter myAdapter = new Adapter(businesses);
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Business> filteredBusiness = myAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
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