package com.fitnesscldev.fitnesscl1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fitnesscldev.fitnesscl1.FragmentCrearEvento.TAG;
import static java.util.Objects.*;

public class FragmentEvento extends Fragment implements FirestoreAdapter.OnListItemClick {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private RecyclerView mEventList;
    private FirebaseFirestore fStore;
    private FirestoreAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseAuth fAuth;
    String userId;
    public FragmentEvento(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mostrarevento,container,false);


        fStore = FirebaseFirestore.getInstance();
        mEventList = view.findViewById(R.id.event_list);


        //query
        Query query = fStore.collection("eventos");

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(5)
                .setPageSize(2)
                .build();


        //Recyvcler
        FirestorePagingOptions<ModelEvento> options = new FirestorePagingOptions.Builder<ModelEvento>()
                .setQuery(query, config, new SnapshotParser<ModelEvento>() {
                    @NonNull
                    @Override
                    public ModelEvento parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        ModelEvento modelEvento = snapshot.toObject(ModelEvento.class);
                        String userId = snapshot.getId();
                        modelEvento.setUserId(userId);
                        return modelEvento;
                    }
                })
                .build();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        mEventList.addItemDecoration(dividerItemDecoration);
        adapter = new FirestoreAdapter(options, this);

        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEventList.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,new FragmentEvento());
                fragmentTransaction.commit();


                swipeRefreshLayout.setRefreshing(false);

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

    @Override
    public void onItemClick(DocumentSnapshot snapshot) {
        String txt1 = snapshot.getId();
        Log.d("ITEM_CLICK","ITEM Seleccionado con id " + txt1);
        FragmentIngresarEvento fragmentIngresarEvento = new FragmentIngresarEvento();
        Bundle bundle = new Bundle();
        bundle.putString("id", txt1);
        fragmentIngresarEvento.setArguments(bundle);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragmentIngresarEvento);
        fragmentTransaction.commit();

    }
    public void content(){
        refresh(3000);
    }

    private void refresh(int milliseconds) {
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }
}


