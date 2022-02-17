package com.fitnesscldev.fitnesscl1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.DeleteDocumentRequest;

public class FragmentEliminarEvento extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView nomEv, direcEv, numPart, DepEv, Noev, txtParti, txtmiEv;
    Button btnDelete;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String userId;
    public static final String TAG = "TAG";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eliminarevento, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        txtParti = view.findViewById(R.id.texParti);
        Noev = view.findViewById(R.id.no_ev);
        Noev.setVisibility(View.GONE);
        nomEv = view.findViewById(R.id.nom_ev);
        direcEv = view.findViewById(R.id.direc_ev);
        numPart = view.findViewById(R.id.nump_ev);
        DepEv = view.findViewById(R.id.tdep_ev);
        txtmiEv = view.findViewById(R.id.txt_ev);
        btnDelete = view.findViewById(R.id.btn_Eliminar);


        DocumentReference documentReference = fStore.collection("eventos").document(userId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        nomEv.setText(document.getString("nombre"));
                        direcEv.setText(document.getString("direccion"));
                        numPart.setText(document.getString("numParti"));
                        DepEv.setText(document.getString("tipoDeporte"));
                    }else {
                        Log.d(TAG, "NO HAY DOCUMENTO");
                        Noev.setVisibility(View.VISIBLE);
                        txtmiEv.setVisibility(View.GONE);
                        btnDelete.setVisibility(View.GONE);
                        direcEv.setVisibility(View.GONE);
                        numPart.setVisibility(View.GONE);
                        nomEv.setVisibility(View.GONE);
                        DepEv.setVisibility(View.GONE);
                        txtParti.setVisibility(View.GONE);
                    }

                }else{
                    Log.d(TAG, "falló con ", task.getException());
                }
            }
        });



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert();

            }
        });






        return view;
    }

    public void Alert(){
        AlertDialog alerta;
        alerta = new AlertDialog.Builder(getContext()).create();
        alerta.setTitle("Confirmación");
        alerta.setMessage("Está seguro que desea eliminar este evento?");
        alerta.setButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  deleteDocumento();
                Toast.makeText(getActivity(), "Evento eliminado", Toast.LENGTH_SHORT).show();
                Noev.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
                direcEv.setVisibility(View.GONE);
                numPart.setVisibility(View.GONE);
                nomEv.setVisibility(View.GONE);
                DepEv.setVisibility(View.GONE);
                txtParti.setVisibility(View.GONE);
                txtmiEv.setVisibility(View.GONE);
            }
        });
        alerta.setButton2("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alerta.show();

    }
    public void deleteDocumento(){

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference dc = fStore.collection("eventos").document(userId);

        dc.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "documento borrado correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error al borrar el documento",e);
            }
        });


    }
}
