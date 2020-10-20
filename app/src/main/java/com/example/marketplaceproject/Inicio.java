package com.example.marketplaceproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {

    private FirebaseUser firebaseUser;
    private String uid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference myRef;
    private Context mContext;
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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        recyclerView = view.findViewById(R.id.rv2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef = FirebaseDatabase.getInstance().getReference();
        dataholder2= new ArrayList<>();
        getDataFromFirebase();
        return view;
    }

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
                            datamodel2.setImageurl(snapshot2.child("imageurl").getValue(String.class));
                            datamodel2.setDescr(snapshot2.child("descripcion").getValue(String.class));
                            datamodel2.setHeader(snapshot2.child("nombre").getValue(String.class));
                            datamodel2.setPrec(snapshot2.child("precio").getValue(String.class));
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
}