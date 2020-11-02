package com.example.marketplaceproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link articulos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class articulos extends Fragment {

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
    private myadapter myadapter;


    RecyclerView recyclerView;
    ArrayList<datamodel> dataholder;

    public articulos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment articulos.
     */
    // TODO: Rename and change types and number of parameters
    public static articulos newInstance(String param1, String param2) {
        articulos fragment = new articulos();
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
        View view = inflater.inflate(R.layout.fragment_articulos, container, false);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef = FirebaseDatabase.getInstance().getReference();
        dataholder= new ArrayList<>();
        getDataFromFirebase();

        //datamodel ob1 = new datamodel("Esta nueva", "5000MXN", "wwsad.adasd", "asdasdsa");
       // dataholder.add(ob1);

        //recyclerView.setAdapter(new myadapter(dataholder));


        return view;
    }



    private void getDataFromFirebase() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        Query query = myRef.child("Usuarios").child(uid).child("articulos");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearAll();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){ //Aquí se obtienen todos los hijos de articulos, o sea todos los articulos.
                    datamodel datamodel = new datamodel();

                    datamodel.setImageurl(snapshot1.child("imageurl").getValue(String.class));
                    datamodel.setDescr(snapshot1.child("descripcion").getValue(String.class));
                    datamodel.setHeader(snapshot1.child("nombre").getValue(String.class));
                    datamodel.setPrec(snapshot1.child("precio").getValue(String.class));
                    datamodel.setPushid(snapshot1.getKey());
                    dataholder.add(datamodel); //se agregan al datamodel.
                }

                myadapter = new myadapter(dataholder);
                recyclerView.setAdapter(new myadapter(dataholder));
                myadapter.notifyDataSetChanged(); //Se señala que hubo un cambio en la información para que el adapter se actualize en tiempo real.
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ClearAll(){
        if(dataholder != null){
            dataholder.clear();

            if(myadapter != null){
                myadapter.notifyDataSetChanged();
            }

        }
        dataholder = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button mbtnNuevoArticulo = view.findViewById(R.id.btnNuevoArticulo);

        mbtnNuevoArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.agregarart);
            }
        });

    }
}