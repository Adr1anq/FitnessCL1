package com.fitnesscldev.fitnesscl1;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {

    public static final String TAG = "TAG";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    Button btnMod;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        LayoutInflater lf = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_main,container,false);

        final EditText name1 = (EditText) view.findViewById(R.id.perNom);
        final EditText email1 = (EditText) view.findViewById(R.id.perEmail);
        final EditText direcc = (EditText) view.findViewById(R.id.perDirecc);
        final EditText telef1 = (EditText) view.findViewById(R.id.perTelef);
        btnMod = view.findViewById(R.id.btn_mod);



        final DocumentReference documentReference = fStore.collection("usuarios").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name1.setText(documentSnapshot.getString("nombre"));
                email1.setText(documentSnapshot.getString("email"));
                direcc.setText(documentSnapshot.getString("direccion"));
                telef1.setText(documentSnapshot.getString("telefono"));


            }
        });

        btnMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Nombre = name1.getText().toString().trim();
                String direc = direcc.getText().toString().trim();
                String email = email1.getText().toString().trim();
                String telef = telef1.getText().toString().trim();

                if(TextUtils.isEmpty(Nombre)){
                    name1.setError("Ingrese un Nombre");
                    return;
                } if(TextUtils.isEmpty(direc)){
                    direcc.setError("Ingrese una Dirección");
                    return;
                } if(TextUtils.isEmpty(email)){
                    email1.setError("Ingrese un Email");
                    return;
                } if(TextUtils.isEmpty(telef)){
                    telef1.setError("Ingrese un Telefono");
                    return;
                }


                userId = fAuth.getCurrentUser().getUid();
                Toast.makeText(getActivity(), "Perfil Modificado Correctamente", Toast.LENGTH_SHORT).show();



                DocumentReference documentReference = fStore.collection("usuarios").document(userId);
                Map<String, Object> eventos = new HashMap<>();
                eventos.put("nombre", Nombre);
                eventos.put("direccion", direc);
                eventos.put("email", email);
                eventos.put("telefono", telef);
                documentReference.set(eventos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"OnSuccess: El Perfil fue Modificado para "+ userId);
                    }
                });
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new FragmentSecond());
                fragmentTransaction.commit();
            }


        });






        return view;
    }
}
