package com.fitnesscldev.fitnesscl1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.database.core.SnapshotHolder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class FragmentIngresarEvento extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    TextView Nom_ev, Direc_ev, NumParti, tipoDep, Anfi;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    public static final String TAG = "TAG";
    String eventId;
    Object object;
    Button btnSalir, btnUnir;


    private RecyclerView mUserList;
    private FirestoreRecyclerAdapter adapter;


    public FragmentIngresarEvento() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingresarevento, container, false);


        Nom_ev = view.findViewById(R.id.nom_ev1);
        Direc_ev = view.findViewById(R.id.direc_ev1);
        NumParti = view.findViewById(R.id.num_parti1);
        tipoDep = view.findViewById(R.id.tipodeporte_ev1);
        Anfi = view.findViewById(R.id.nom_anfiev);
        btnSalir = view.findViewById(R.id.btn_salir);
        btnUnir = view.findViewById(R.id.btn_unir);

        Bundle bundle = getArguments();
        String userId = bundle.getString("id");


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        mUserList = view.findViewById(R.id.user_list);

        

        final DocumentReference documentReference = fStore.collection("eventos").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        eventId = documentSnapshot.getString("eventoId");
                        Log.d(TAG, "el id es : " + eventId);
                    }
                }

            }
        });



        DocumentReference dc = fStore.collection("usuarios").document(userId);

        Query query = fStore.collection("usuarios").whereEqualTo("evId", "B4e545yBFingt7WkLOzU");

        FirestoreRecyclerOptions<ModelUser> options = new FirestoreRecyclerOptions.Builder<ModelUser>()
                .setQuery(query, ModelUser.class)
                .build();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        mUserList.addItemDecoration(dividerItemDecoration);


        adapter = new FirestoreRecyclerAdapter<ModelUser, UsersViewHolder>(options) {
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull ModelUser model) {

                holder.list_nom.setText(model.getNombre());
                holder.list_email.setText(model.getEmail());

                Log.d(TAG, "ERROR" + eventId);

            }
        };

        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUserList.setAdapter(adapter);


        dc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot dc = task.getResult();
                    Anfi.setText(dc.getString("nombre"));
                } else {

                }
            }
        });


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Nom_ev.setText(document.getString("nombre"));
                    Direc_ev.setText(document.getString("direccion"));
                    NumParti.setText(document.getString("numParti"));
                    tipoDep.setText(document.getString("tipoDeporte"));


                } else {
                    Log.d(TAG, "NO HAY DOCUMENTO");
                }
            }
        });


        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new FragmentEvento());
                fragmentTransaction.commit();
            }
        });

        btnUnir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentuser = fAuth.getCurrentUser().getUid();
                DocumentReference documentRef = fStore.collection("usuarios").document(currentuser);
                fStore.collection("usuarios").document(currentuser).update("evId", "B4e545yBFingt7WkLOzU");
                mUserList.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private class UsersViewHolder extends RecyclerView.ViewHolder {
        private TextView list_nom, list_email;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            list_nom = itemView.findViewById(R.id.nom_user);
            list_email = itemView.findViewById(R.id.email_user);
        }
    }


}
