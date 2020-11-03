package com.example.marketplaceproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment{
    private FirebaseUser firebaseUser;
    private String uid;
    TextView txtLong, txtLat;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationManager locationManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference myRef, onlineRef, currentUserRef, usuarioslocalizacionRef;
    private GeoFire geoFire;
    private Context mContext;
    ValueEventListener onlineValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                currentUserRef.onDisconnect().removeValue();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Snackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    };
    private myadapter2 myadapter2;

    RecyclerView recyclerView;
    ArrayList<datamodel2> dataholder2;


    public Inicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inicio.
     */
    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void showSettingsDialog() { //Mensaje de dialogo por si no acepta los permisos
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Permiso requerido");
        builder.setMessage("Esta aplicación necesita el permiso solicitado para funcionar.");
        builder.setPositiveButton("Ir a configuración", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() { // Ir al menu de configuración de la app
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onDestroy() {
        geoFire.removeLocation(FirebaseAuth.getInstance().getCurrentUser().getUid());
        onlineRef.removeEventListener(onlineValueEventListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerOnlyneSystem();
    }

    private void registerOnlyneSystem() {
        onlineRef.addValueEventListener(onlineValueEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        usuarioslocalizacionRef = FirebaseDatabase.getInstance().getReference("UsuariosLocation");
        currentUserRef = FirebaseDatabase.getInstance().getReference("UsuariosLocation").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        geoFire = new GeoFire(usuarioslocalizacionRef);
        registerOnlyneSystem();


        txtLong = view.findViewById(R.id.txtLongitud);
        txtLat = view.findViewById(R.id.txtLatitud);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Permisos para la ubicación.
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                                3, mLocationListener);
                        Toast.makeText(getContext(), "Estás en línea", Toast.LENGTH_SHORT).show();
                        /*Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                        onLocationChanged(location); Este sirve para obtener la ubicación una sola vez*/


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(), "Permiso denegado "+permissionDeniedResponse.getPermissionName(), Toast.LENGTH_SHORT).show();
                        showSettingsDialog();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

        recyclerView = view.findViewById(R.id.rv2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef = FirebaseDatabase.getInstance().getReference();
        dataholder2= new ArrayList<>();
        getDataFromFirebase();
        return view;
    }
    //https://www.androidhive.info/2015/02/android-location-api-using-google-play-services/



    private void getDataFromFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        Query query = myRef.child("Usuarios");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearAll();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){ //Aquí se obtienen todos los hijos de articulos, o sea todos los articulos.
                    String currentUser = snapshot1.getKey();
                    Log.d(TAG, "Usuario ID actual: "+ uid);
                    Log.d(TAG, "Todos los ID: " + currentUser);
                    /**/

                    if(!snapshot1.getKey().equals(uid)){
                        //String currentArticulos = snapshot1.child("articulos").getKey();
                        //Log.d(TAG, "Articulos push.id: " + currentArticulos);
                        for(DataSnapshot snapshot2 : snapshot1.child("articulos").getChildren()){
                            String currentArticulos = snapshot2.getKey();
                            Log.d(TAG, "Articulos push.id: " + currentArticulos);
                            datamodel2 datamodel2 = new datamodel2();
                            //Life: Si quieres agregar un dato como el nombre de usuario o correo, sácalo del
                            //snapshot 1 (como el bloque de comentario abajo, lit igualito).
                            datamodel2.setPushid(currentArticulos);
                            datamodel2.setImageurl(snapshot2.child("imageurl").getValue(String.class));
                            datamodel2.setDescr(snapshot2.child("descripcion").getValue(String.class));
                            datamodel2.setHeader(snapshot2.child("nombre").getValue(String.class));
                            datamodel2.setPrec(snapshot2.child("precio").getValue(String.class));
                            datamodel2.setUserid(snapshot1.getKey());
                            String nombreCompleto = snapshot1.child("nombre").getValue(String.class);
                            nombreCompleto = nombreCompleto + " " + snapshot1.child("apellido").getValue(String.class);
                            datamodel2.setUsuario("En venta por: "+nombreCompleto);
                            dataholder2.add(datamodel2); //se agregan al datamodel.

                        }
                            /*
                            datamodel2 datamodel2 = new datamodel2();
                            datamodel2.setImageurl(snapshot1.child("profilepic").child("imageurl").getValue(String.class));
                            datamodel2.setDescr(snapshot1.child("telefono").getValue(String.class));
                            //String nombreCompleto = snapshot1.child("nombre").getValue(String.class);
                            //nombreCompleto = nombreCompleto + " " + snapshot1.child("apellido").getValue(String.class);
                            //datamodel2.setHeader(nombreCompleto);
                            dataholder2.add(datamodel2); //se agregan al datamodel.
                            */
                    }else{
                        Log.d(TAG, "Los 2 valores");
                    }

                }
                myadapter2 = new myadapter2(dataholder2);
                recyclerView.setAdapter(new myadapter2(dataholder2));
                myadapter2.notifyDataSetChanged(); //Se señala que hubo un cambio en la información para que el adapter se actualize en tiempo real.
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ClearAll() {
        if(dataholder2 != null){
            dataholder2.clear();

            if(myadapter2 != null){
                myadapter2.notifyDataSetChanged();
            }

        }
        dataholder2 = new ArrayList<>();
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            txtLat.setText("Latitud: "+latitude);
            txtLong.setText("Longitud: "+longitude);
            geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude),
                    (key, error) -> {
                        if(error != null)
                            Snackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_LONG).show();

                    });
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    /* Recuerda agregar el Implements LocationListener
    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        txtLat.setText("Latitud: "+latitude);
        txtLong.setText("Longitud: "+longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

     */
}