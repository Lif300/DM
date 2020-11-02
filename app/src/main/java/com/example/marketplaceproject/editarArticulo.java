package com.example.marketplaceproject;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editarArticulo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editarArticulo extends Fragment {
    private String precio, nombre, pushid, desc, nombre2, precio2, desc2, nombre3, precio3, desc3, uid;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference imageload = firebaseDatabase.getReference();
    private StorageReference storageReference;
    private StorageReference Folder;
    DatabaseReference imagestore;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int ImageBack = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public editarArticulo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editarArticulo.
     */
    // TODO: Rename and change types and number of parameters
    public static editarArticulo newInstance(String param1, String param2) {
        editarArticulo fragment = new editarArticulo();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(), ""+nombre+desc+precio+pushid, Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_editar_articulo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText mtxtNombre = view.findViewById(R.id.txtNombreArtEdit);
        EditText mtxtDesc = view.findViewById(R.id.txtDescripcionArtEdit);
        EditText mtxtPrecio = view.findViewById(R.id.txtPrecioEdit);
        Button btnEditar = view.findViewById(R.id.btnGuardarArt);
        Button btnFotoart = view.findViewById(R.id.btnEditFotoArt);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        Folder = FirebaseStorage.getInstance().getReference().child(uid).child("ArticulosPics");

        DatabaseReference first = imageload.child("Usuarios").child(uid).child("articulos").child(pushid).child("imageurl");
        first.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.i("TAG", "No hay foto: "+uid+" "+pushid);
                }else{
                    String link = snapshot.getValue(String.class);
                    CircleImageView artEdit = view.findViewById(R.id.fotoEditArt);
                    Glide.with(getContext()).load(link).into(artEdit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mtxtNombre.setText(nombre);
        nombre2 = mtxtNombre.getText().toString();
        mtxtDesc.setText(desc);
        desc2 = mtxtDesc.getText().toString();
        precio2 = precio;

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre3 = mtxtNombre.getText().toString();
                desc3 = mtxtDesc.getText().toString();
                precio3 = mtxtPrecio.getText().toString();
                if(mtxtNombre.getText().toString().isEmpty() || mtxtDesc.getText().toString().isEmpty() || mtxtPrecio.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "No se puede actualizar la información, favor de llenar los campos", Toast.LENGTH_SHORT).show();
                }else{
                    if(!nombre2.matches(nombre3) || !desc2.matches(desc3) || !precio2.matches(precio3)){
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        uid = firebaseUser.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(uid).child("articulos").child(pushid);
                        databaseReference.child("nombre").setValue(nombre3);
                        databaseReference.child("descripcion").setValue(desc3);
                        databaseReference.child("precio").setValue(precio3+"MXN");
                        Toast.makeText(getContext(), "Información del artículo actualizada", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.articulos);
                }

                }
            }
        });

        btnFotoart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,ImageBack);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ImageBack){
            if(resultCode == RESULT_OK){

                Uri ImageData = data.getData();

                StorageReference Imagename = Folder.child("image"+ImageData.getLastPathSegment());
                Imagename.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imagestore = FirebaseDatabase.getInstance().getReference();
                                Map<String, String> Map = new HashMap<>();
                                Map.put("imageurl", String.valueOf(uri));
                                imagestore.child("Usuarios").child(uid).child("articulos").child(pushid).setValue(Map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }
}