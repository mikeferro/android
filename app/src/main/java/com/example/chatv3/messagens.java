package com.example.chatv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class Messagens extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagens);

        verificarSigin();
    }

    private void verificarSigin()
    {
        if(FirebaseAuth.getInstance().getUid()==null)
        {
            Intent intent = new Intent(Messagens.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

      @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch(item.getItemId())
        {
            case R.id.contactos:
                break;
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                verificarSigin();
                break;
        }
        return  super.onOptionsItemSelected(item);
    }
}
