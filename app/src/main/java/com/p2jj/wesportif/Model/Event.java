package com.p2jj.wesportif.Model;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.Objects;

public class Event {

    private int id,capacite;
    private String titre, categorieSport;
    private String discription;
    private String img_event;
    private String date_debut;
    private String date_fin,lieu,userCreator;

    public Bitmap getImgBmp() {
        return imgBmp;
    }

    public void setImgBmp(Bitmap imgBmp) {
        this.imgBmp = imgBmp;
    }

    private Bitmap imgBmp;

    public Event() {
    }

    public int getCapacite() {
        return capacite;
    }

    public String getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", categorieSport='" + categorieSport + '\'' +
                ", discription='" + discription + '\'' +
                ", img_event='" + img_event + '\'' +
                ", date_debut='" + date_debut + '\'' +
                ", date_fin='" + date_fin + '\'' +
                ", imgBmp=" + imgBmp +
                '}';
    }

    public Event(String titre, String img_event, String date_debut) {
        this.titre = titre;
        this.img_event = img_event;
        this.date_debut = date_debut;
    }

    public Event(String titre, String img_event, String date_debut, String categorieSport) {
        this.titre = titre;
        this.img_event = img_event;
        this.date_debut = date_debut;
        this.categorieSport = categorieSport;
    }

    public String getCategorieSport() {
        return categorieSport;
    }

    public void setCategorieSport(String categorieSport) {
        this.categorieSport = categorieSport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImg_event() {
        return img_event;
    }

    public void setImg_event(String img_event) {
        this.img_event = img_event;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id;
    }


}
