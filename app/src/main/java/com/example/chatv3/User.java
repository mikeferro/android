package com.example.chatv3;

public class User {


        public String username;
        public String uid;
        public String email;
        public String fotoperfil;


        public User(String email, String uid, String username,String fotoperfil) {
            this.uid = uid;
            this.email = email;
            this.username = username;
            this.fotoperfil=fotoperfil;

        }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoperfil() {
        return fotoperfil;
    }

    public void setFotoperfil(String fotoperfil) {
        this.fotoperfil = fotoperfil;
    }
}


