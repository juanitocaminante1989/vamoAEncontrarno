package com.example.dalonso.vamoaencontrarno.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by dalonso on 02/08/2016.
 */
public class Message implements Serializable {

    String id;
    private String chatRoomId;
    String message;
    String createdAt;
    private String perfil;
    User user;
    private Bitmap photo;

    public Message() {
    }

    public Message(String id, String message, String createdAt, String perfil, User user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.setPerfil(perfil);
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
