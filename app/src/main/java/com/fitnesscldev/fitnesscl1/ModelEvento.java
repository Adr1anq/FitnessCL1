package com.fitnesscldev.fitnesscl1;

import android.view.Display;

public class ModelEvento {

    private String nombre, direccion, numParti, tipoDeporte, eventoId, userId;

    public ModelEvento() {}
    public ModelEvento(String nombre, String direccion, String numParti, String tipoDeporte, String eventoId, String userId){

        this.nombre = nombre;
        this.direccion = direccion;
        this.numParti = numParti;
        this.tipoDeporte = tipoDeporte;
        this.eventoId = eventoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumParti() {
        return numParti;
    }

    public void setNumParti(String numParti) {
        this.numParti = numParti;
    }

    public String getTipoDeporte() {
        return tipoDeporte;
    }

    public void setTipoDeporte(String tipoDeporte) {
        this.tipoDeporte = tipoDeporte;
    }




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }
}
