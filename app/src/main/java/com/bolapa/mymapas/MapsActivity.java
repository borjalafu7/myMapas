package com.bolapa.mymapas;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationClient;
    private double mLatitude = 0.0, mLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private List<Marker> mPosiciones = new ArrayList<>();

    Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(16000);
        locationRequest.setFastestInterval(8000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();

                        LatLng posicion_actualizada = new LatLng(mLatitude, mLongitude);
                        mPosiciones.add(mMap.addMarker(new MarkerOptions().position(posicion_actualizada).title("Mi localización").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_fotomia))));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion_actualizada));

                        //Toast.makeText(MapsActivity.this, "Lat: " + mLatitude+ " Lon: " + mLongitude, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };


        //Material Drawer

        new DrawerBuilder().withActivity(this).build();

        //builder de la cabecera del materialdrawer
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.ic_launcher)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("myMapas v1.0")
                                .withEmail("borjaalafu@gmail.com")
                                .withIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                )
                .build();

        //Elementos del materialdrawer
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withDrawerGravity(Gravity.START)
                .withSelectedItem(3)
                .withSliderBackgroundColor(getResources().getColor(android.R.color.white))
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName("Ver mi localización"),
                        new SecondaryDrawerItem()
                                .withIdentifier(2)
                                .withName("Ocultar mi localización"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem()
                                .withIdentifier(3)
                                .withName("Ver Bares"),
                        new PrimaryDrawerItem()
                                .withIdentifier(4)
                                .withName("Ver Tiendas"),
                        new SecondaryDrawerItem()
                                .withIdentifier(5)
                                .withName("Ocultar todo"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIdentifier(6)
                                .withName("Cerrar menú"),
                        new SecondaryDrawerItem()
                                .withIdentifier(7)
                                .withName("Salir App")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1: {
                                setear_miubicacion();
                                mMap.setOnInfoWindowClickListener(MapsActivity.this);
                                break;
                            }
                            case 2: {
                                for (Marker posicion:mPosiciones){
                                    posicion.remove();
                                }
                                removeLocations();
                                break;
                            }
                            case 3: {
                                ver_makers("bares");
                                mMap.setOnInfoWindowClickListener(MapsActivity.this);
                                break;
                            }
                            case 4: {
                                ver_makers("tiendas");
                                mMap.setOnInfoWindowClickListener(MapsActivity.this);
                                break;
                            }
                            case 5: {
                                mMap.clear();
                                break;
                            }
                            case 6: {
                                break;
                            }
                            case 7: {
                                finish();
                                break;
                            }
                        }
                        return false;
                    }
                }).build();


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
        LatLng alaquas = new LatLng(39.4569586, -0.4607805);
        //LatLng castellon_latitud = new LatLng(40, 0);


        //Marker castellon = mMap.addMarker(new MarkerOptions().position(castellon_latitud).title("Parque del Meridiano").snippet("Habitantes:180000").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ciudad)));
        //castellon.setTag("Ciudad");

        //para ocultar el marcador
        //ayuntamiento_de_alaquas.hideInfoWindow();


        //mueve la camara a ese punto al iniciar la app
        mMap.moveCamera(CameraUpdateFactory.newLatLng(alaquas));

        //establece un zoom
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));

        //setea el minimo y el maximo posible de zoom
        mMap.setMinZoomPreference(5.0f);
        mMap.setMaxZoomPreference(20.0f);

        //cuando mueves la camara sale un toast con el zoom que tienes puesto
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //Toast.makeText(MapsActivity.this,"Zoom cambiado a: "+ mMap.getCameraPosition().zoom, Toast.LENGTH_SHORT).show();
            }
        });

        //cambia el tipo de mapa
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //añade los controles +/- para controlar el zoom
        //mMap.getUiSettings().setZoomControlsEnabled(true);

        //quita la brujula que aparece al rotar el mapa
        mMap.getUiSettings().setCompassEnabled(false);

        //prohibe la posibilidad de rotar el mapa con gestos táctiles
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        //asigna un listener al mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Toast.makeText(MapsActivity.this,"Mapa pulsado", Toast.LENGTH_SHORT).show();
            }
        });

        //cambia el estilo de mapa con un json
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style));


        //habilitar la localizacion en la que te encuentres
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }

        //quitar el icono de localizacion de arriba a la derecha
        mMap.setMyLocationEnabled(false);

        Location castellon_location = new Location("castellon");
        castellon_location.setLatitude(40.0);
        castellon_location.setLongitude(0.0);

        //Toast.makeText(MapsActivity.this,"Distancia: "+ String.format("%2f", location.distanceTo(castellon_location)/1000)+ " Km", Toast.LENGTH_SHORT).show();

        //Toast.makeText(MapsActivity.this, "Tit(1):" + leerLocalizaciones().get(1).getTitulo(), Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTitle().equals("Mi localización")){
            mostrardialogo(MapsActivity.this);
        }else{
            switch (marker.getTag().toString()){
                case "bares":
                    Intent intent_web = new Intent(Intent.ACTION_VIEW, Uri.parse(marker.getSnippet()));
                    if ( intent_web.resolveActivity(getPackageManager()) != null){
                        startActivity(intent_web);
                    }
                    break;

                case "tiendas":
                    Intent intent_llamada = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: "+marker.getSnippet()));
                    if (intent_llamada.resolveActivity(getPackageManager()) != null){
                        startActivity(intent_llamada);
                    }
                    break;
            }
        }

    }

    private void requestLocations() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            mMap.setOnInfoWindowClickListener(MapsActivity.this);
        } else {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            mMap.setOnInfoWindowClickListener(MapsActivity.this);
        }

    }

    private void removeLocations() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public List<Localizacion> leerLocalizaciones() {
        List<Localizacion> localizaciones = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(getResources().openRawResource(R.raw.localizaciones));
            Element raiz = doc.getDocumentElement();
            NodeList items = raiz.getElementsByTagName("localizacion");

            for (int i = 0; i < items.getLength(); i++) {
                Node nodoLocalizacion = items.item(i);
                Localizacion localizacion = new Localizacion();

                for (int j = 0; j < nodoLocalizacion.getChildNodes().getLength() - 1; j++) {
                    Node nodoActual = nodoLocalizacion.getChildNodes().item(j);
                    if (nodoActual.getNodeType() == Node.ELEMENT_NODE) {
                        if (nodoActual.getNodeName().equalsIgnoreCase("titulo")) {
                            localizacion.setTitulo(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("fragmento")) {
                            localizacion.setFragmento(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("etiqueta")) {
                            localizacion.setEtiqueta(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("latitud")) {
                            String latitud = nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setLatitud(Double.parseDouble(latitud));
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("longitud")) {
                            String longitud = nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setLongitud(Double.parseDouble(longitud));
                        }
                    }
                }
                localizaciones.add(localizacion);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localizaciones;
    }

    public void mostrardialogo(MapsActivity view) {

        MiDialogo md = new MiDialogo(this, mLatitude, mLongitude, new MiDialogo.RespuestaDialogo() {
            @Override
            public void OnAccept(String cadena) {
                Toast.makeText(MapsActivity.this, cadena, Toast.LENGTH_SHORT).show();
            }
        });

        md.MostrarDialogoBotones().show();
    }

    public void ver_makers(String etiqueta){
        List<Localizacion> mLocalizaciones;
        mLocalizaciones = leerLocalizaciones();
        for (Localizacion localizacion : mLocalizaciones) {
            if (etiqueta.equals("bares")){
                if (localizacion.getEtiqueta().equals("bares")) {
                    LatLng posicion_bares = new LatLng(localizacion.getLatitud(), localizacion.getLongitud());
                    Marker localizacion_bares = mMap.addMarker(new MarkerOptions().position(posicion_bares).title(localizacion.getTitulo()).snippet(localizacion.getFragmento()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bares)));
                    localizacion_bares.setTag(localizacion.getEtiqueta());
                }
            }else if (etiqueta.equals("tiendas")){
                if (localizacion.getEtiqueta().equals("tiendas")) {
                    LatLng posicion_tiendas = new LatLng(localizacion.getLatitud(), localizacion.getLongitud());
                    Marker localizacion_tiendas = mMap.addMarker(new MarkerOptions().position(posicion_tiendas).title(localizacion.getTitulo()).snippet(localizacion.getFragmento()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tiendas)));
                    localizacion_tiendas.setTag(localizacion.getEtiqueta());
                }
            }

        }
    }

    public void setear_miubicacion(){
        LatLng localizacion_latitud = new LatLng(mLatitude, mLongitude);
        Marker localizacion_marker = mMap.addMarker(new MarkerOptions().position(localizacion_latitud).title("Mi localización").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_fotomia)));
        Log.i("DEBUG"," "+mLatitude+" "+mLongitude);
        localizacion_marker.setTag("posicion_mia");
        mPosiciones.add(localizacion_marker);

        requestLocations();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(localizacion_latitud));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
    }


}