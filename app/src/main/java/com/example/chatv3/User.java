package com.example.chatv3;

import android.widget.EditText;

public class User {


        public EditText username;
        public String uid;
        public EditText email;
        public String fotoperfil;


        public User(EditText email, String uid, EditText username, String fotoperfil) {
            this.uid = uid;
            this.email = email;
            this.username = username;
            this.fotoperfil=fotoperfil;

        }


}


