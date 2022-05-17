package com.example.businessclientapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterBusiness extends AppCompatActivity {

    EditText businessName;
    EditText email;
    EditText phoneNumber;
    EditText location;
    EditText password;
    Button registerBusiness;
    ProgressBar progressBar;
    TextView backToLogin;
    private FirebaseAuth mAuth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business);

        mAuth = FirebaseAuth.getInstance();

        businessName = (EditText) findViewById(R.id.businessName);
        email = (EditText) findViewById(R.id.businessEmail);
        phoneNumber = (EditText) findViewById(R.id.bPhoneNumber);
        location = (EditText) findViewById(R.id.bLocation);
        password = (EditText) findViewById(R.id.bPassword);
        registerBusiness = (Button) findViewById(R.id.registerBusiness);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        backToLogin = (TextView) findViewById(R.id.bLogin2);

        registerBusiness.setOnClickListener(v -> {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            String businessName_text = businessName.getText().toString();
            String email_text = email.getText().toString();
            String phoneNumber_text = phoneNumber.getText().toString();
            String location_text = location.getText().toString();
            String password_text = password.getText().toString();

            Business businessObject = new Business(email_text, businessName_text, location_text, Integer.parseInt(phoneNumber_text));

            signUpClient(email_text, password_text, businessObject);
        });

        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

    }

    public void signUpClient(String email, String password, Business business) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            reference.child(user.getUid()).setValue(business);

                            Snackbar.make(getCurrentFocus(), business.getBusinessName() + " registered successfully", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.YELLOW)
                                    .show();
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterBusiness.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
}