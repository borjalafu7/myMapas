package com.bolapa.mymapas;

import android.content.Context;
import android.view.LayoutInflater;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

    public class MiDialogo {
        Context context;
        RespuestaDialogo rd;
        double mLatitude = 0.0, mLongitude = 0.0;

        public MiDialogo(Context context, double mLatitude, double mLongitude, RespuestaDialogo rd){
            this.context = context;
            this.rd = rd;
            this.mLatitude = mLatitude;
            this.mLongitude = mLongitude;
        }

        /* DIALOGO CON BOTONES CLASICO*/
        public Dialog MostrarDialogoBotones(){

            //Creamos el constructor de dialogos
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            //Añadimos parámetros que necesitemos para el dialogo
            builder.setTitle("Su ubicación actual es:")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("Lat: "+mLatitude+" Lon: "+mLongitude)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Lo que hace al aceptar
                            //Toast.makeText(context,"Has aceptado",Toast.LENGTH_SHORT).show();
                            rd.OnAccept("Has aceptado");
                        }
                    });
            //devuelves el builder creandolo
            return builder.create();
        }

        public interface RespuestaDialogo{
            void OnAccept(String cadena);
        }
}
