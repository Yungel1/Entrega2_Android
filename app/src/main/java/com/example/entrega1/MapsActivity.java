package com.example.entrega1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback/*, LocationListener*/ {

    //private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private String usuario;
    private Bundle extras;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Conseguir el tema desde las preferencias y aplicarlo
        int tema = this.getTema();
        setTheme(tema);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Obtenemos el usuario desde la actividad UsuariosActivity
        extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        //Inicialización del mapa
        SupportMapFragment elfragmento = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentoMapa);
        elfragmento.getMapAsync(this);

        //Geolocalización, conseguir el fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Checkear los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/
    }

    //Cuando se den los permisos recrear la actividad para que funcione conrrectamente la geolocalización
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Checkear los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Conseguir la última localización
                        if (location != null) {
                            //Añadir el marcador en la posición actual, con el nombre del usuario que ha iniciado sesión
                            LatLng locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.addMarker(new MarkerOptions()
                                    .position(locationLatLng)
                                    .title(usuario));

                            //Zoom a la posición
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,15));
                            // Animando la cámara.
                            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        }
                    }
                });
    }

    private int getTema(){

        //Obtener el tema desde las SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String color = prefs.getString("listPref",null);
        if (color==null){
            color = "orange";
        }
        int tema;
        switch (color) {
            case "blue":
                tema = R.style.Theme_TemaAzul;
                break;
            default:
                tema = R.style.Theme_TemaNaranja;
        }
        return tema;
    }


    /*@Override
    public void onLocationChanged(@NonNull Location location) {

    }*/


}