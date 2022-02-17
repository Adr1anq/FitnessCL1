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

public class FragmentoModificarEvento extends Fragment {

    public static final String TAG = "TAG";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button editBt;
    TextView Noev1, edEv;
    EditText edNombre, edDirec, edNumPar;
    String userId;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_modificarevento, container, false);
        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        edEv = view.findViewById(R.id.edEvento);
        Noev1 = view.findViewById(R.id.ed_ev1);
        Noev1.setVisibility(View.GONE);
        edNombre = view.findViewById(R.id.ednombre_evento);
        edDirec = view.findViewById(R.id.eddireccion_evento);
        edNumPar = view.findViewById(R.id.ednumparti_evento);
        editBt = view.findViewById(R.id.mEdit);


        final Spinner spinner = view.findViewById(R.id.spDeporte);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.tipoDeporte,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        DocumentReference documentReference = fStore.collection("eventos").document(userId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        edNombre.setText(document.getString("nombre"));
                        edDirec.setText(document.getString("direccion"));
                        edNumPar.setText(document.getString("numParti"));

                    }else{
                        Log.d(TAG, "NO HAY DOCUMENTO");
                        Noev1.setVisibility(View.VISIBLE);
                        editBt.setVisibility(View.GONE);
                        edNombre.setVisibility(View.GONE);
                        edDirec.setVisibility(View.GONE);
                        edNumPar.setVisibility(View.GONE);
                        edEv.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);

                    }

                }
            }
        });







        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String evNombre = edNombre.getText().toString().trim();
                final String evDireccion = edDirec.getText().toString().trim();
                final String evNumPart = edNumPar.getText().toString().trim();
                String tipoDeporte = spinner.getSelectedItem().toString();
                boolean piv=false;


                if(TextUtils.isEmpty(evNombre)){
                    edNombre.setError("Ingrese un Nombre para su Evento");
                    return;
                }
                if(TextUtils.isEmpty(evDireccion)){
                    edDirec.setError("Ingrese Una Dirección");
                    return;
                }
                if(TextUtils.isEmpty(evNumPart)){
                    edNumPar.setError("Ingrese la Cantidad de Participantes");
                    return;
                }

                userId = fAuth.getCurrentUser().getUid();
                Toast.makeText(getActivity(), "Evento Modificado", Toast.LENGTH_SHORT).show();



                DocumentReference documentReference = fStore.collection("eventos").document(userId);
                Map<String, Object> eventos = new HashMap<>();
                eventos.put("nombre", evNombre);
                eventos.put("direccion", evDireccion);
                eventos.put("numParti", evNumPart);
                eventos.put("tipoDeporte", tipoDeporte);
                documentReference.set(eventos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"OnSuccess: EL Evento fue Modificado para "+ userId);
                    }
                });









            }
        });









        return view;

    }
}

