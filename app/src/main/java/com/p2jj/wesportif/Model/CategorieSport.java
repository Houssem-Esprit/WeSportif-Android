package com.p2jj.wesportif.Model;

public class CategorieSport {

    private int idCategorie;
    private  String nameSport;
    private Boolean value;

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public CategorieSport( int idCategorie,String nameSport, Boolean value) {
        this.idCategorie=idCategorie;
        this.nameSport = nameSport;
        this.value = value;
    }
    public CategorieSport( int idCategorie,String nameSport) {
        this.idCategorie=idCategorie;
        this.nameSport = nameSport;
    }

    @Override
    public String toString() {
        return  nameSport;
    }

    public CategorieSport() {

    }

    public String getNameSport() {
        return nameSport;
    }

    public void setNameSport(String nameSport) {
        this.nameSport = nameSport;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
