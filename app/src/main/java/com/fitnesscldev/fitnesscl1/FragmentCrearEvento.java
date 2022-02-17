package com.fitnesscldev.fitnesscl1;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FragmentCrearEvento extends Fragment {
    public static final String TAG = "TAG";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView crEvento,noCrear;
    Button crearEv;
    EditText crNombre, crDirec, crNumPar;
    String userId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_crearevento, container, false);



        crNombre = view.findViewById(R.id.nombre_evento);
        crDirec = view.findViewById(R.id.direccion_evento);
        crNumPar = view.findViewById(R.id.numparti_evento);
        crEvento = view.findViewById(R.id.crEvento);
        noCrear = view.findViewById(R.id.no_crear);

        crearEv = view.findViewById(R.id.mCrear);

        noCrear.setVisibility(View.GONE);

        final Spinner spinner = view.findViewById(R.id.spDeporte);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.tipoDeporte,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String tipoDeporte = spinner.getSelectedItem().toString();

        userId = fAuth.getCurrentUser().getUid();



        DocumentReference documentReference = fStore.collection("eventos").document(userId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){
                            Log.d(TAG,"Ya existe un documento con este usuario");
                            noCrear.setVisibility(View.VISIBLE);
                            crNombre.setVisibility(View.GONE);
                            crDirec.setVisibility(View.GONE);
                            crNumPar.setVisibility(View.GONE);
                            crEvento.setVisibility(View.GONE);
                            spinner.setVisibility(View.GONE);
                            crearEv.setVisibility(View.GONE);

                        }else{

                        }
                    }
            }
        });


        crearEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String evNombre = crNombre.getText().toString().trim();
                final String evDireccion = crDirec.getText().toString().trim();
                final String evNumPart = crNumPar.getText().toString().trim();
                String tipoDeporte = spinner.getSelectedItem().toString();
                boolean piv=false;


                if(TextUtils.isEmpty(evNombre)){
                    crNombre.setError("Ingrese un Nombre para su Evento");
                    return;
                }
                if(TextUtils.isEmpty(evDireccion)){
                    crDirec.setError("Ingrese Una Dirección");
                    return;
                }
                if(TextUtils.isEmpty(evNumPart)){
                    crNumPar.setError("Ingrese la Cantidad de Participantes");
                    return;
                }

                userId = fAuth.getCurrentUser().getUid();
                Toast.makeText(getActivity(), "Evento Creado", Toast.LENGTH_SHORT).show();

                final String autoId = fStore.collection("eventos").document().getId();

                DocumentReference documentReference = fStore.collection("eventos").document(userId);
                Map<String, Object> eventos = new HashMap<>();
                eventos.put("nombre", evNombre);
                eventos.put("direccion", evDireccion);
                eventos.put("numParti", evNumPart);
                eventos.put("tipoDeporte", tipoDeporte);
                eventos.put("eventoId", autoId);

                DocumentReference dc1 = fStore.collection("usuarios").document(userId);
                fStore.collection("usuarios").document(userId).update("evId", "B4e545yBFingt7WkLOzU");


                documentReference.set(eventos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"OnSuccess: EL Evento fue creado para "+ userId);
                        Log.d(TAG,"OnSuccess: Este es el id "+ autoId);
                    }
                });

                crNombre.setText("");
                crDirec.setText("");
                crNumPar.setText("");

                noCrear.setVisibility(View.VISIBLE);
                crNombre.setVisibility(View.GONE);
                crDirec.setVisibility(View.GONE);
                crNumPar.setVisibility(View.GONE);
                crEvento.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                crearEv.setVisibility(View.GONE);





            }
        });









        return view;

    }
}
