package com.example.dalonso.vamoaencontrarno.rest;

/**
 * Created by dalonso on 02/08/2016.
 */
public class EndPoints {



    public static final String BASE_URL_TEST = "http://192.168.1.131/gcm_chat/v1"; //servidor localhost
    public static final String LOGIN = BASE_URL_TEST + "/user/login";
    public static final String USER = BASE_URL_TEST + "/user/_ID_";
    public static final String SEND_LOCATION = BASE_URL_TEST + "/users/send_location";
    public static final String CREATE_LOCATION = BASE_URL_TEST + "/localizaciones/add_location";
    public static final String UPDATE_LOCATION = BASE_URL_TEST + "/users/update_location/_ID_";
    public static final String LOCATIONS = BASE_URL_TEST + "/users/all_locations";


}
