package com.example.entrega1;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //EL widget mostrará la hora, que se actualizará cada 30 minutos
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
        int minuto = calendario.get(Calendar.MINUTE);
        //Si por ejemplo a la hora de actualizar son las 11:28 se mostrarán las 11:00
        if(minuto >= 30){
            //En cambio si son las 11:32 se mostrarán las 12:00
            calendario.add(Calendar.HOUR, 1);
        }
        calendario.set(Calendar.MINUTE, 0);
        String horaconformato = formato.format(calendario.getTime());
        views.setTextViewText(R.id.appwidget_text, context.getString(R.string.hour)+" "+horaconformato);

        //Actualizar el widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Actualizar los widget (en este caso 1)
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}