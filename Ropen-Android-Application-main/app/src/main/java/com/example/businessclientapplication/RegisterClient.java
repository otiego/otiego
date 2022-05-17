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

public class RegisterClient extends AppCompatActivity {

    private EditText fullName;
    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private Button registerClient;
    private TextView backToLogin;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        mAuth = FirebaseAuth.getInstance();

        fullName = (EditText) findViewById(R.id.fullName);
        email = (EditText) findViewById(R.id.businessEmail);
        phoneNumber = (EditText) findViewById(R.id.bPhoneNumber);
        password = (EditText) findViewById(R.id.bPassword);
        registerClient = (Button) findViewById(R.id.registerClient);
        backToLogin = (TextView) findViewById(R.id.bLogin1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        registerClient.setOnClickListener(v -> {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            String email_text = email.getText().toString();
            String password_text = password.getText().toString();
            String fullName_text = fullName.getText().toString();
            String phoneNumber_text = phoneNumber.getText().toString();

            Client clientObject = new Client(email_text, Integer.parseInt(phoneNumber_text), fullName_text);

            signUpClient(email_text, password_text, clientObject);
        });

        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }


    public void signUpClient(String email, String password, Client client) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            reference.child(user.getUid()).setValue(client);

                            Snackbar.make(getCurrentFocus(), client.getFullName() + " registered successfully", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.YELLOW)
                                    .show();
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterClient.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }


}