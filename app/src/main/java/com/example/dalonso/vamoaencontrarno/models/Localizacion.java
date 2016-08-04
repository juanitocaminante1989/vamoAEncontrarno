package com.example.dalonso.vamoaencontrarno.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by dalonso on 03/08/2016.
 */
public class Localizacion implements Serializable, ClusterItem {

    private String localizacion_id;
    private User user;
    private String latitud;
    private String longitud;
    private String created_at;
    private LatLng mPosition;

    public Localizacion() {
    }

    public Localizacion(String latitud, String longitud){
        mPosition = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));

    }

    public Localizacion(String localizacion_id, User user, String latitud, String longitud) {
        this.localizacion_id = localizacion_id;
        this.user = user;
        this.latitud = latitud;
        this.longitud = longitud;
        mPosition = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));

    }

    public String getLocalizacion_id() {
        return localizacion_id;
    }

    public void setLocalizacion_id(String localizacion_id) {
        this.localizacion_id = localizacion_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
