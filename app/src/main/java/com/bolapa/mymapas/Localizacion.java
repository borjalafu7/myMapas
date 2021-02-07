package com.bolapa.mymapas;

public class Localizacion {

    private String titulo;
    private String fragmento;
    private String etiqueta;
    private double latitud;
    private double longitud;

    public Localizacion(String titulo, String fragmento, String etiqueta, double latitud, double longitud) {
        this.titulo = titulo;
        this.fragmento = fragmento;
        this.etiqueta = etiqueta;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Localizacion(){
        this.titulo = "";
        this.fragmento = "";
        this.etiqueta = "";
        this.latitud = 0;
        this.longitud = 0;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFragmento() {
        return fragmento;
    }

    public void setFragmento(String fragmento) {
        this.fragmento = fragmento;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }


}
