package com.fitnesscldev.fitnesscl1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mNombre,mApellidoPat,mApellidoMat,mEmail,mPassword,mPhone,mDireccion;
    Button mRegistrarBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);


        mNombre = findViewById(R.id.nombre);
        mApellidoPat = findViewById(R.id.apellidoPat);
        mApellidoMat = findViewById(R.id.apellidoMater);
        mDireccion = findViewById((R.id.direccion));
        mEmail =  findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegistrarBtn = findViewById(R.id.registrarBtn);
        final String evId = " ";

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegistrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String nombre = mNombre.getText().toString().trim();
                final String ApPat = mApellidoPat.getText().toString().trim();
                final String ApMat = mApellidoMat.getText().toString().trim();
                final String Telefono = mPhone.getText().toString().trim();
                final String Direccion = mDireccion.getText().toString().trim();

                //Validaciones para los campos de registrar usuario

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Ingrese un Email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Ingrese su Contraseña");
                    return;
                }
                if(TextUtils.isEmpty(nombre)){
                    mNombre.setError("Ingrese Su nombre");
                    return;
                }
                if(TextUtils.isEmpty(ApMat)){
                    mApellidoMat.setError("Ingrese Su  Apellido Materno");
                    return;
                }if(TextUtils.isEmpty(ApPat)){
                    mApellidoPat.setError("Ingrese Su  Apellido Paterno");
                    return;
                }
                if(TextUtils.isEmpty(Telefono)){
                    mApellidoPat.setError("Ingrese Su  Telefono");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Se requiere una contraseña con 6 caracteres minimo");
                    return;
                }

                //Registrar usuario en la cloud firestore

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Registrar.this, "Usuario Creado", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            /*hace referencia al documento dentro de la coleccion en la cloud firestore
                            luego se crea la collecion en el caso de no tener una y andentro de está coleccion el documento */
                            DocumentReference documentReference = fStore.collection("usuarios").document(userID);
                            Map<String, Object> usuario = new HashMap<>();
                            usuario.put("nombre",nombre);
                            usuario.put("apePat",ApPat);
                            usuario.put("apeMat",ApMat);
                            usuario.put("email",email);
                            usuario.put("telefono",Telefono);
                            usuario.put("direccion", Direccion);
                            usuario.put("evId", evId);
                            documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"OnSuccess: EL perfil de usuario fue creado para "+ userID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),Login.class));

                        }else{
                            Toast.makeText(Registrar.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });



    }


    public void Login(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}
