package com.example.marketplaceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {

    private EditText mtxtNombre;
    private EditText mtxtCorreo;
    private EditText mtxtContra;
    private EditText mtxtApellido;
    private Button mbtnConfirmar, mbtnRegresar;

    //Variables de los datos que se registrarán
    private String nombre= "";
    private String apellido= "";
    private String correo= "";
    private String contra= "";

    //Objeto Firebase Auth para la autenticación
    private FirebaseAuth fAuth;
    private DatabaseReference fDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference();

        mtxtNombre = (EditText) findViewById(R.id.txtNombre);
        mtxtApellido = (EditText) findViewById(R.id.txtApellido);
        mtxtCorreo = (EditText) findViewById(R.id.txtCorreo);
        mtxtContra = (EditText) findViewById(R.id.txtContra);
        mbtnConfirmar = (Button) findViewById(R.id.btnConfirmar);
        mbtnRegresar = (Button) findViewById(R.id.btnRegresar);

        mbtnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = mtxtNombre.getText().toString();
                apellido = mtxtApellido.getText().toString();
                correo = mtxtCorreo.getText().toString();
                contra = mtxtContra.getText().toString();

                if (!nombre.isEmpty() && !correo.isEmpty() && !contra.isEmpty() && !apellido.isEmpty()){
                    if(contra.length() >= 6){
                        registrarUsuario();
                    }else{
                        Toast.makeText(RegistrarActivity.this, "La contraseña debe contener al 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistrarActivity.this, "Favor de introducir todos los datos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mbtnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registrarUsuario(){
        fAuth.createUserWithEmailAndPassword(correo, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = fAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("apellido", apellido);
                    map.put("correo", correo);
                    map.put("contraseña", contra);

                    fDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                Toast.makeText(RegistrarActivity.this, "El usuario se registró correctamente.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
                                finish();
                            }else{
                                Toast.makeText(RegistrarActivity.this, "No se pudo registrar al usuario correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegistrarActivity.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}