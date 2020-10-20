package com.example.marketplaceproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;



public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mbtnRegistrar;
    private Button mbtnIniciar;
    private EditText mtxtCorreo;
    private EditText mtxtContra;

    //Variables de los datos con los que se iniciará sesión
    private String email= "";
    private String pass= "";

    //Objeto Firebase Auth para la autenticación
    private FirebaseAuth fAuth;
    private DatabaseReference fDatabase;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();

        mtxtCorreo= (EditText) findViewById(R.id.txtEmail);
        mtxtContra = (EditText) findViewById(R.id.txtPass);
        mbtnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        mbtnIniciar = (Button) findViewById(R.id.btnIniciar);

       mbtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
                finish();
            }
        });
       mbtnIniciar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               email = mtxtCorreo.getText().toString();
               pass = mtxtContra.getText().toString();
               if(!email.isEmpty() && !pass.isEmpty()){
                    SignInWithEmailAndPassword(email, pass);
               }else{
                   Toast.makeText(LoginActivity.this, "Favor de llenar todos los datos", Toast.LENGTH_SHORT).show();
               }

           }
       });
    }

    public void SignInWithEmailAndPassword(String email, String password){
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = fAuth.getCurrentUser();
                            updateUI(user);
                            if(user.isEmailVerified()) {
                                Log.d("Inicio de sesión", "Exitoso");
                                startActivity(new Intent(LoginActivity.this, SideBarActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Favor de verificar su cuenta.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                           Log.d("Inicio de sesión", "Fallido");
                            Toast.makeText(LoginActivity.this, "Contraseña/Correo incorrectos.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fAuth.getCurrentUser();
        updateUI(currentUser);
        if(currentUser != null){
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void updateUI(FirebaseUser user) {
        Log.i("User", "CurrentUser: "+currentUser);
    }


}