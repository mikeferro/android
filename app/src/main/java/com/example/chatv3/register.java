package com.example.chatv3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class register extends AppCompatActivity {

    private EditText email, password, username;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button btn_foto;
    private Uri CaminhoFoto;
    private ImageView cfoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);
        username = findViewById(R.id.main_username);
        btn_foto = (findViewById(R.id.btn_foto));
        cfoto = (findViewById(R.id.img_circular));


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        email.setText("teste1@ipca.pt");
        password.setText("123456");


        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecinarFoto();

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 0) {
            CaminhoFoto = data.getData();

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), CaminhoFoto);
                cfoto.setImageDrawable(new BitmapDrawable(bitmap));
                // esconder o btn
                btn_foto.setAlpha(0);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private void selecinarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }

    public void onClickRegister(View view) {


        String email = this.email.getText().toString();
        String pass = this.password.getText().toString();
        String username = this.username.getText().toString();
        String hashPass = hashPassword(pass);

        if (username == null || username.isEmpty() || email == null || email.isEmpty() || pass == null || pass.isEmpty()) {
            Toast.makeText(this, "Username,email e password nao preenchidos", Toast.LENGTH_SHORT).show();
            return;
        } else {

            mAuth.createUserWithEmailAndPassword(email, hashPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.i("Teste", task.getResult().getUser().getUid());
                        SaveUser();
                    }



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Teste", e.getMessage());
                }
            });


        }
    }

    private void SaveUser() {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref= FirebaseStorage.getInstance().getReference("/images" + filename);
        ref.putFile(CaminhoFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("teste", uri.toString());

                        String uid =mAuth.getUid();
                        String fotoperfil = uri.toString();

                        User user= new User(email,uid,username,fotoperfil);

                        
                        mDatabase.child("users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i("teste", documentReference.getId());
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("teste", e.getMessage());
                                    }
                                });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("teste", e.getMessage(), e);
            }
        });

        //final DatabaseReference ref = mDatabase("/image" + filename);
    }



    private String hashPassword(String pass) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pass.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            String hex = String.format("%064x", new BigInteger(1, digest));
            return hex;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return pass;

        }
    }
}



