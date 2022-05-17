package com.example.businessclientapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BusinessView extends AppCompatActivity {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");

    private TextView businessName;
    private TextView businessLocation;
    private TextView UserName;
    private Switch isOpened;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_view);

        businessName = (TextView) findViewById(R.id.bvBusinessName);
        businessLocation = (TextView) findViewById(R.id.bvLocation);
        UserName = (TextView) findViewById(R.id.bvUserName);
        isOpened = (Switch) findViewById(R.id.bvIsOpened);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String user_email = getIntent().getStringExtra("USER_EMAIl");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Business mBusiness = snapshot.getValue(Business.class);

                businessName.setText(mBusiness.businessName);
                businessLocation.setText(mBusiness.location);
                UserName.setText(String.valueOf(mBusiness.phoneNumber));
                isOpened.setChecked(mBusiness.opened);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });





        isOpened.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpened.isChecked()) {
                    reference.child(user.getUid()).child("opened").setValue(true);
                } else {
                    reference.child(user.getUid()).child("opened").setValue(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}