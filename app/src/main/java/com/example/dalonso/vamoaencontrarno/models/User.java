package com.example.dalonso.vamoaencontrarno.models;

import java.io.Serializable;

/**
 * Created by dalonso on 02/08/2016.
 */
public class User implements Serializable {
    String id;
    String name;
    private String passwd;
    private String email;
    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String id, String name, String email, String passwd){
        this.id = id;
        this.name= name;
        this.setEmail(email);
        this.setPasswd(passwd);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
