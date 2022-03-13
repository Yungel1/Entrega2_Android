package com.example.entrega1;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Preferencias extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_LIST_PREFERENCE = "listPref";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_config);
    }

    public void insertarPreferencia(SharedPreferences prefs,String color){

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LIST_PREFERENCE, color);
        editor.apply();
    }

    public String leerPreferencia(SharedPreferences prefs){

        String color = prefs.getString(KEY_LIST_PREFERENCE,null);
        return color;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        /*String color = this.leerPreferencia(sharedPreferences);
        int tema;
        //Toast toast = Toast.makeText(getActivity(), "Ajustes guardados", Toast.LENGTH_LONG);
        //toast.show();
        switch (color){
            case "blue":
                tema = R.style.Theme_TemaAzul;
                break;
            default:
                tema = R.style.Theme_TemaNaranja;
        }*/
        LoginActivity.la.recreate();
        Intent i = new Intent (getActivity(), LoginActivity.class);
        //i.putExtra("tema",tema);
        startActivity(i);
    }
}