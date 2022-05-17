package com.example.businessclientapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button Login;
    private TextView registerClient;
    private TextView registerBusiness;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.businessEmail);
        password = (EditText) findViewById(R.id.bPassword);
        Login = (Button) findViewById(R.id.signIn);
        registerBusiness = (TextView) findViewById(R.id.registerAsBusiness);
        registerClient = (TextView) findViewById(R.id.registerAsClient);

        Login.setOnClickListener(v -> {
            String email_text = email.getText().toString();
            String password_text = password.getText().toString();

            signIn(email_text, password_text);
        });

        registerClient.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterClient.class);
            startActivity(intent);
        });

        registerBusiness.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterBusiness.class);
            startActivity(intent);
        });
    }


    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Authenticated Successfully", Toast.LENGTH_LONG).show();
                            getUserStatus(user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    void getUserStatus(String email) {
        AtomicBoolean userStatus = new AtomicBoolean(false);
        reference.orderByKey().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot result = task.getResult();
                Iterator<DataSnapshot> iterator = result.getChildren().iterator();

                while(iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    User value = (User) next.getValue(User.class);
                    if (email.equals(value.email)) {
                        ChangeLayout(value.status, email);
                        break;
                    }
                }
            }
        });
    }

    private void ChangeLayout(boolean status, String email) {
        Intent intent;
        if(status) {
            intent = new Intent(getApplicationContext(), ClientView.class);
        } else {

            intent = new Intent(getApplicationContext(), BusinessView.class);
            intent.putExtra("USER_EMAIL", email);
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        finish();
    }
}