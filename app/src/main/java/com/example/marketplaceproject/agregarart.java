package com.example.marketplaceproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link agregarart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class agregarart extends Fragment {
    private StorageReference Folder;
    private String nombreArt = "";
    private String precioArt = "";
    private String descripcionArt = "";
    private DatabaseReference fDatabase;
    private FirebaseAuth fAuth;
    private String uid;
    private FirebaseUser firebaseUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button mbtnFoto;
    CircleImageView mfotoArt;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Bitmap bitmap = null;
    private static final int ImageBack = 1;

    public agregarart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment agregarart.
     */
    // TODO: Rename and change types and number of parameters
    public static agregarart newInstance(String param1, String param2) {
        agregarart fragment = new agregarart();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_agregarart, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();

        Folder = FirebaseStorage.getInstance().getReference().child(uid).child("ArticulosPics");
        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        mfotoArt = view.findViewById(R.id.fotoArt);

        EditText mtxtNombreArt =(EditText)view.findViewById(R.id.txtNombreArt);
        EditText mtxtPrecioArt = (EditText)view.findViewById(R.id.txtPrecio);
        EditText mtxtDescripcionArt = (EditText)view.findViewById(R.id.txtDescripcionArt);



        Button mbtnAgregarArt = view.findViewById(R.id.btnAgregarArt);
        mbtnAgregarArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nombreArt = mtxtNombreArt.getText().toString();
                precioArt = mtxtPrecioArt.getText().toString();
                descripcionArt = mtxtDescripcionArt.getText().toString();

                if (!nombreArt.isEmpty() && !precioArt.isEmpty() && !descripcionArt.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, ImageBack);
                    //guardarArticulo();
                    Navigation.findNavController(v).navigate(R.id.articulos);

                } else {
                    Toast.makeText(getContext(), "Favor de llenar todos los campos.", Toast.LENGTH_SHORT).show();
                }
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
                                DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference();
                                String id = fAuth.getCurrentUser().getUid();
                                Map<String, Object> map = new HashMap<>();
                                map.put("imageurl", String.valueOf(uri));
                                map.put("nombre", nombreArt);
                                map.put("descripcion", descripcionArt);
                                map.put("precio", precioArt+"MXN");
                                imagestore.child("Usuarios").child(id).child("articulos").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Articulo añadido", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

    private void guardarArticulo() {
        String id = fAuth.getCurrentUser().getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombreArt);
        map.put("descripcion", descripcionArt);
        map.put("precio", precioArt);
        fDatabase.child("Usuarios").child(id).child("articulos").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Articulo:", "Añadido correctamente");
                    /* FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, articulos);
                    transaction.commit();*/
                } else {
                    Log.d("Articulo:", "No añadido");
                }
            }
        });
    }
}