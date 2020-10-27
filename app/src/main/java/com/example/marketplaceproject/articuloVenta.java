package com.example.marketplaceproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link articuloVenta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class articuloVenta extends Fragment  {
    private String precio, nombre, pushid, desc, userid;
    private FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference imageload = firebaseDatabase.getReference();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public articuloVenta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment articuloVenta.
     */
    // TODO: Rename and change types and number of parameters
    public static articuloVenta newInstance(String param1, String param2) {
        articuloVenta fragment = new articuloVenta();
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
            nombre = getArguments().getString("nombre");
            desc = getArguments().getString("desc");
            precio = getArguments().getString("precio");
            pushid = getArguments().getString("pushid");
            userid = getArguments().getString("userid");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Toast.makeText(getContext(), ""+nombre+desc+precio+pushid, Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_articulo_venta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button mbtnArticuloVenta = view.findViewById(R.id.btnVerInformación);
        TextView mtxtNombreArt = view.findViewById(R.id.txtLabelNombre);
        TextView mtxtPrecio = view.findViewById(R.id.txtLabelPrecio);
        TextView descripcion = view.findViewById(R.id.txtLabelDesc);
        mtxtNombreArt.setText(nombre);
        descripcion.setText(desc);
        mtxtPrecio.setText(precio);


        DatabaseReference first = imageload.child("Usuarios").child(userid).child("articulos").child(pushid).child("imageurl");
        first.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.i("TAG", "No hay foto: "+userid+" "+pushid);
                }else{
                    String link = snapshot.getValue(String.class);
                    CircleImageView mProfilePic = getView().findViewById(R.id.fotoArtVendido);
                    Glide.with(getContext()).load(link).into(mProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        mbtnArticuloVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("precio", precio);
                bundle.putString("pushid", pushid);
                bundle.putString("nombre", nombre);
                Navigation.findNavController(v).navigate(R.id.perfilVendedor, bundle);
            }
        });

    }


}