package com.fitnesscldev.fitnesscl1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirestoreAdapter extends FirestorePagingAdapter<ModelEvento, FirestoreAdapter.EventoViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapter(@NonNull FirestorePagingOptions<ModelEvento> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull EventoViewHolder holder, int position, @NonNull ModelEvento model) {
        holder.nom_ev.setText(model.getNombre());
        holder.direc_ev.setText(model.getDireccion());
        holder.numParti_ev.setText(model.getNumParti());
        holder.tDepo_ev.setText(model.getTipoDeporte());
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_eventos, parent, false);
        return new EventoViewHolder(view);
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nom_ev, direc_ev, numParti_ev, tDepo_ev;
        private Button ing_ev;


        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            nom_ev = itemView.findViewById(R.id.Nom_Ev);
            direc_ev = itemView.findViewById(R.id.direc_Ev);
            numParti_ev = itemView.findViewById(R.id.numparti_ev);
            tDepo_ev = itemView.findViewById(R.id.dep_Ev);
            ing_ev = itemView.findViewById(R.id.btn_ing);

            ing_ev.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()));
        }
    }
    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot snapshot);
    }

}
