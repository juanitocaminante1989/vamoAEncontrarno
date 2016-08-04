package com.example.dalonso.vamoaencontrarno;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dalonso.vamoaencontrarno.gcm.GcmIntentService;
import com.example.dalonso.vamoaencontrarno.helper.Config;
import com.example.dalonso.vamoaencontrarno.helper.MyPreferenceManager;
import com.example.dalonso.vamoaencontrarno.models.Localizacion;
import com.example.dalonso.vamoaencontrarno.models.Message;
import com.example.dalonso.vamoaencontrarno.models.User;
import com.example.dalonso.vamoaencontrarno.rest.EndPoints;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private LocationManager locationManager;
    private Location locati;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private final String LOG_TAG = "MapsActivity";
    private static MapsActivity mInstance;
    private RequestQueue mRequestQueue;
    private MyPreferenceManager pref;
    private Context mContext;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//    private ImageButton panicButton;
    private Button panicButton, panicButtonGirl, panicButtonFight, panicButtonDrink;
    private String firstMessage;

    private ArrayList<Localizacion> localizaciones;
    private ArrayList<Marker> markers;

    @Override
    public void onLocationChanged(Location location) {
        locati = location;
        Log.e(LOG_TAG, "latitud: " + locati.getLatitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mInstance = this;
        localizaciones = new ArrayList<>();
        markers = new ArrayList<>();
        initGCMNotifications();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if(getPrefManager().getUser() == null){
            createFirstTimeUSer();

        }else{

        }
        locati = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5000, this);
        if (checkPlayServices()) {
            registerGCM();
        }
        //panicButton = (ImageButton) findViewById(R.id.panicButton);
        panicButton = (Button) findViewById(R.id.panicButton);
        panicButtonDrink = (Button) findViewById(R.id.panicButtonDrink);
        panicButtonGirl = (Button) findViewById(R.id.panicButtonPerrea);
        panicButtonFight = (Button) findViewById(R.id.panicButtonFight);
        panicButton.setVisibility(View.VISIBLE);

//        panicButton = (FloatingActionButton) findViewById(R.id.panicButton);
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPrefManager().getUser() != null){
                    panicButton.setVisibility(View.GONE);
                    panicButtonDrink.setVisibility(View.GONE);
                    panicButtonGirl.setVisibility(View.GONE);
                    panicButtonFight.setVisibility(View.GONE);
                    sendLocationToAll("2");
                }else{
                    createFirstTimeUSer();
                }
            }
        });
        panicButtonDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPrefManager().getUser() != null){
                    panicButton.setVisibility(View.GONE);
                    panicButtonDrink.setVisibility(View.GONE);
                    panicButtonGirl.setVisibility(View.GONE);
                    panicButtonFight.setVisibility(View.GONE);
                    sendLocationToAll("3");
                }else{
                    createFirstTimeUSer();
                }
            }
        });

        panicButtonGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPrefManager().getUser() != null){
                    panicButton.setVisibility(View.GONE);
                    panicButtonDrink.setVisibility(View.GONE);
                    panicButtonGirl.setVisibility(View.GONE);
                    panicButtonFight.setVisibility(View.GONE);
                    sendLocationToAll("4");
                }else{
                    createFirstTimeUSer();
                }
            }
        });
        panicButtonFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPrefManager().getUser() != null){
                    panicButton.setVisibility(View.GONE);
                    panicButtonDrink.setVisibility(View.GONE);
                    panicButtonGirl.setVisibility(View.GONE);
                    panicButtonFight.setVisibility(View.GONE);
                    sendLocationToAll("5");
                }else{
                    createFirstTimeUSer();
                }
            }
        });
    }

    private void sendLocationToAll(final String tipoPush) {

        String endPoint = EndPoints.UPDATE_LOCATION.replace("_ID_", getPrefManager().getUser().getId());
        Log.e(LOG_TAG, "endpoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.PUT,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {

                        fetchLocations();

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(LOG_TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitud", String.valueOf(locati.getLatitude()));
                params.put("longitud", String.valueOf(locati.getLongitude()));
                params.put("message", ": Entre tarea y tarea pitillo y perrea");
                params.put("tipoPush", tipoPush);

                Log.e(LOG_TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        addToRequestQueue(strReq);

    }

    private void fetchLocations() {

        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.LOCATIONS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("localizaciones");
                        localizaciones.clear();
                        markers.clear();
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject locationObj = (JSONObject) chatRoomsArray.get(i);
                            Localizacion l = new Localizacion();
                            l.setLocalizacion_id(locationObj.getString("localizacion_id"));
                            l.setLatitud(locationObj.getString("latitud"));
                            l.setLongitud(locationObj.getString("longitud"));
                            l.setCreated_at(locationObj.getString("created_at"));

                            String userId = locationObj.getString("user_id");
                            String userName = locationObj.getString("nickname");
                            User user = new User(userId, userName);
                            l.setUser(user);

                            localizaciones.add(l);
                            LatLng newPosition = new LatLng(Double.parseDouble(l.getLatitud()), Double.parseDouble(l.getLongitud()));
                            MarkerOptions ma = new MarkerOptions()
                                .position(newPosition).title(l.getUser().getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                            Marker m = mMap.addMarker(ma);
                            m.showInfoWindow();
                            markers.add(m);
                            float zoomLevel = 18.0f; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, zoomLevel));



                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                // subscribing to all chat room topics
                subscribeToAllTopics();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(LOG_TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Adding request to request queue
        getInstance().addToRequestQueue(strReq);

    }
    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    private void subscribeToAllTopics() {

        if(localizaciones != null && this != null){

            if(localizaciones != null){
                for (Localizacion item : localizaciones) {
                    Localizacion l = item;
                    Intent intent = new Intent(this, GcmIntentService.class);
                    intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
                    intent.putExtra(GcmIntentService.TOPIC, "topic_" + l.getLocalizacion_id());
                    this.startService(intent);
                }
            }

        }

    }


    // subscribing to global topic
    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        startService(intent);
    }
    private void login(final String userName) {
       /* if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }*/
        final String email = userName +"@encontrarno.com";//inputEmail.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // user successfully logged in


                        JSONObject userObj = obj.getJSONObject("user");
                        User user = new User(userObj.getString("user_id"),
                                userObj.getString("name"),
                                userObj.getString("email"),
                                userObj.getString("passwd"));

                        // storing user in shared preferences
                        getPrefManager().storeUser(user);
                        registerGCM();

                        createLocation();

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(LOG_TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", userName);
                params.put("email", email);
                params.put("passwd", "1234");

                Log.e(LOG_TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        addToRequestQueue(strReq);
    }

    public boolean isGPSAllow() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng pisoFranco = new LatLng(38.537248, -0.114625);
        LatLng myPosition = new LatLng(locati.getLatitude(), locati.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Piso Franco"));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pisoFranco, zoomLevel));*/
    }



    public static synchronized MapsActivity getInstance() {
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? LOG_TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(LOG_TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /*public void logout() {
        pref.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }*/

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }
    public boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(LOG_TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        fetchLocations();
    }

    private void initGCMNotifications(){
        /**
         * Broadcast receiver calls in two scenarios
         * 1. gcm registration is completed
         * 2. when new push notification is received
         * */
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Log.e(LOG_TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };
    }

    /**
     * Handles new push notification
     */
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);
        Message message = (Message) intent.getSerializableExtra("message");
        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            if(!message.getUser().getId().equals(getPrefManager().getUser().getId())){
                //Toast.makeText(getApplicationContext(), "New push: " + message.getId() + " " +message.getChatRoomId(), Toast.LENGTH_LONG).show();
            }
            for (Marker m : markers){
                if(m.getTitle().equals(message.getUser().getName())){
                    m.remove();
                    Snackbar.make(panicButton, message.getUser().getName() + " se ha movido",  Snackbar.LENGTH_SHORT).show();
                    LatLng newPosition = new LatLng(Double.parseDouble(message.getId()), Double.parseDouble(message.getChatRoomId()));
                    m.setPosition(newPosition);
                    m.showInfoWindow();
                }
//                MarkerOptions mark = new MarkerOptions().position(new LatLng(Double.parseDouble(message.getId()),Double.parseDouble( message.getChatRoomId())));
//                mMap.addMarker(mark);
//                m.remove();
            }

            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LatLng newPosition = new LatLng(Double.parseDouble(message.getId()), Double.parseDouble(message.getChatRoomId()));
            MarkerOptions ma = new MarkerOptions()
                    .position(newPosition)
                    .title(message.getUser().getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            *//*MarkerOptions mark = new MarkerOptions()
                    .title("Task")
                    .position(newPosition);*//*
            mMap.addMarker(ma);*/
        }

        if(type == Config.PUSH_TYPE_USER_COPA) {
            for (Marker m : markers) {
                if (m.getTitle().equals(message.getUser().getName())) {
                    m.remove();
                    Snackbar.make(panicButton, message.getUser().getName() + " se ha movido", Snackbar.LENGTH_SHORT).show();
                    LatLng newPosition = new LatLng(Double.parseDouble(message.getId()), Double.parseDouble(message.getChatRoomId()));
                    m.setPosition(newPosition);
                    m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_drink));
                    m.showInfoWindow();
                }
            }
        }
            if (type == Config.PUSH_TYPE_USER_PERREA) {
                for (Marker m : markers) {
                    if (m.getTitle().equals(message.getUser().getName())) {
                        m.remove();
                        Snackbar.make(panicButton, message.getUser().getName() + " se ha movido", Snackbar.LENGTH_SHORT).show();
                        LatLng newPosition = new LatLng(Double.parseDouble(message.getId()), Double.parseDouble(message.getChatRoomId()));
                        m.setPosition(newPosition);
                        m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_girls));
                        m.showInfoWindow();
                    }
                }
            }
            if (type == Config.PUSH_TYPE_USER_BRONCA) {
                for (Marker m : markers) {
                    if (m.getTitle().equals(message.getUser().getName())) {
                        m.remove();
                        Snackbar.make(panicButton, message.getUser().getName() + " se ha movido", Snackbar.LENGTH_SHORT).show();
                        LatLng newPosition = new LatLng(Double.parseDouble(message.getId()), Double.parseDouble(message.getChatRoomId()));
                        m.setPosition(newPosition);
                        m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_fight));
                        m.showInfoWindow();
                    }
                }
            }

    }


    private void createFirstTimeUSer(){

        final AlertDialog builder = new AlertDialog.Builder(this, R.style.sendMessageDialog)
                .setPositiveButton("SEND", null)
                .setNegativeButton("CANCEL", null)
                .create();
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(32, 0, 32, 0);
        final EditText etNickName = new EditText(this);
        etNickName.setLayoutParams(lp);
        etNickName.setTextColor(Color.WHITE);
        ll.addView(etNickName, lp);
        builder.setView(ll);
        builder.setTitle("NICKNAME");
        builder.setMessage("Introduce tu nombre para encontrarno");
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etNickName.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter a user name", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("ProfilesFragment", "You have entered: " + etNickName.getText().toString());
                            builder.dismiss();
                            firstMessage = etNickName.getText().toString();
                            login(firstMessage);
                        }
                    }
                });

                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ProfilesFragment", "Invitation declined");
                        builder.dismiss();
                    }
                });
            }
        });

                        /* Show the dialog */
        builder.show();
    }

    private void createLocation() {



        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CREATE_LOCATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, "response: " + response);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(LOG_TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getPrefManager().getUser().getId());
                params.put("nickname", getPrefManager().getUser().getName());
                params.put("latitud", String.valueOf(locati.getLatitude()));
                params.put("longitud", String.valueOf(locati.getLongitude()));

                Log.e(LOG_TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        addToRequestQueue(strReq);
    }



    private class LoadLatitud extends AsyncTask<Void, Void, Double>{

        @Override
        protected Double doInBackground(Void... params) {
            return locati.getLatitude();
        }

        @Override
        protected void onPostExecute(Double d){
            locati.setLatitude(d);
        }
    }

    @Override
    public void onBackPressed() {
        panicButton.setVisibility(View.VISIBLE);
        panicButtonDrink.setVisibility(View.VISIBLE);
        panicButtonGirl.setVisibility(View.VISIBLE);
        panicButtonFight.setVisibility(View.VISIBLE);

    }
}
