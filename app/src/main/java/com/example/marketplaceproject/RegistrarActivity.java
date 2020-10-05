package com.example.marketplaceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {

    private EditText mtxtNombre, mtxtCorreo, mtxtContra, mtxtContraConf, mtxtApellido, mtxtTel;
    private Button mbtnConfirmar, mbtnRegresar;
    private TextView txtvisibleono;

    //Variables de los datos que se registrarán
    private String nombre= "";
    private String apellido= "";
    private String correo= "";
    private String contra= "";
    private String tel= "";
    private String contraconf ="";

    //Objeto Firebase Auth para la autenticación
    private FirebaseAuth fAuth;
    private DatabaseReference fDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference();

        txtvisibleono = (TextView) findViewById(R.id.visibleono);
        mtxtTel = (EditText) findViewById(R.id.txtTel);
        mtxtNombre = (EditText) findViewById(R.id.txtNombre);
        mtxtApellido = (EditText) findViewById(R.id.txtApellido);
        mtxtCorreo = (EditText) findViewById(R.id.txtCorreo);
        mtxtContra = (EditText) findViewById(R.id.txtContra);
        mtxtContraConf = (EditText) findViewById(R.id.txtContraConf);
        mbtnConfirmar = (Button) findViewById(R.id.btnConfirmar);
        mbtnRegresar = (Button) findViewById(R.id.btnRegresar);

        mbtnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = mtxtNombre.getText().toString();
                apellido = mtxtApellido.getText().toString();
                correo = mtxtCorreo.getText().toString();
                contra = mtxtContra.getText().toString();
                contraconf = mtxtContraConf.getText().toString();
                tel = mtxtTel.getText().toString();

                if (!nombre.isEmpty() && !correo.isEmpty() && !contra.isEmpty() && !apellido.isEmpty() && !contraconf.isEmpty()){
                   if(contra.matches(contraconf)){
                       if(contra.length() >= 6){
                           registrarUsuario();
                       }else{
                           Toast.makeText(RegistrarActivity.this, "La contraseña debe contener al 6 caracteres", Toast.LENGTH_SHORT).show();
                       }
                   }else{
                       txtvisibleono.setVisibility(View.VISIBLE);
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
                    Log.d("TAG", "createUserWithEmail:success");

                    FirebaseUser user = fAuth.getCurrentUser();
                    updateUI(user);


                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("telefono", tel);
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
                                user.sendEmailVerification();
                            }else{
                                Toast.makeText(RegistrarActivity.this, "No se pudo registrar al usuario correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                   Log.w("TAG", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegistrarActivity.this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        updateUI (currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
            Log.i("User:", ""+currentUser);
    }


}
