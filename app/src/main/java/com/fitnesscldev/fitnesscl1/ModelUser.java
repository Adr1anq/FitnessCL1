package com.fitnesscldev.fitnesscl1;

public class ModelUser {

    private String nombre, apeMat, apePat, direccion, email, telefono, evId;

    public ModelUser(){}

    public ModelUser(String nombre, String apeMat, String apePat, String direccion, String email, String telefono, String evId){
        this.nombre = nombre;
        this.apeMat = apeMat;
        this.apePat = apePat;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.evId = evId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getEvId() {
        return evId;
    }

    public void setEvId(String evId) {
        this.evId = evId;
    }
}
