package com.example.entrega1;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class AdaptadorListView extends BaseAdapter {

    private Context contexto;
    private LayoutInflater inflater;
    private ArrayList<String> usuarios;
    private int image;


    public AdaptadorListView(Context pcontext, ArrayList<String> pusuarios, int pimage) {
        contexto = pcontext;
        usuarios = pusuarios;
        image = pimage;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int i) {
        return usuarios.get(i);
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
        usuario.setText(usuarios.get(i));

        //onClick del boton
        botonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Acceso al gestor de la base de datos
                MiBD gestorDB = new MiBD(viewGroup.getContext(), "Users", null, 1);
                //Iniciar la siguiente actividad
                Intent intent = new Intent(viewGroup.getContext(), MailActivity.class);
                intent.putExtra("usuarioAñadido",usuarios.get(i));
                viewGroup.getContext().startActivity(intent.putExtra("idioma",UsuariosActivity.idioma));
            }
        });

        return view;
    }
}
