package com.example.chatv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText email,password, username;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);
        username = findViewById(R.id.main_username);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        email.setText("teste1@ipca.pt");
        password.setText("123456");

    }

    //https://firebase.google.com/docs/auth/android/password-auth
    public void onClickLogin(View view) {
        String email = this.email.getText().toString();
        String pass = this.password.getText().toString();
        String hashPass = hashPassword(pass);



        mAuth.signInWithEmailAndPassword(email, hashPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUser(user.getUid());
                            Toast.makeText(MainActivity.this,"Parabéns!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplication(),messagens.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"Má Sorte!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    public String hashPassword(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );
            md.update( pass.getBytes( StandardCharsets.UTF_8 ) );
            byte[] digest = md.digest();
            String hex = String.format( "%064x", new BigInteger( 1, digest ) );
            return hex;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return pass;
        }

    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(getApplication(), register.class);
        startActivity(intent);




    }



    private void getUser(String uid) {

        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,String> obj =  (HashMap<String,String>) dataSnapshot.getValue();
                username.setText(obj.get("username"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
