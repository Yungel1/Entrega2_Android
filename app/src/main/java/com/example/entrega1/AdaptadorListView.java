package com.example.entrega1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdaptadorListView extends BaseAdapter {

    private Context contexto;
    private LayoutInflater inflater;
    private String[] botones;
    private String[] usuarios;
    private int image;


    public AdaptadorListView(Context pcontext, String[] pboton, String[] pusuarios, int pimage) {
        contexto = pcontext;
        usuarios = pusuarios;
        botones = pboton;
        image = pimage;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return usuarios.length;
    }

    @Override
    public Object getItem(int i) {
        return usuarios[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view=inflater.inflate(R.layout.lista_usuarios,null);
        TextView usuario = (TextView) view.findViewById(R.id.textView);
        ImageButton botonMail = (ImageButton) view.findViewById(R.id.mailBTN);

        botonMail.setImageResource(image);
        usuario.setText(usuarios[i]);
        //botonMail.setText(botones[i]);

        //onClick del boton
        botonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(contexto, "Seleccionaste "+usuarios[i],
                        Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
