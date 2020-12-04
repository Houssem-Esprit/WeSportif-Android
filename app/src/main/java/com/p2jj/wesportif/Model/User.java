package com.p2jj.wesportif.Model;

import java.util.Date;

public class User {

    private String cin;
    private String nom, prenom,login,pass,email,role, user_categorie;
    private String date_naissance,coverImg,img_user;
    private int numTelephone;

    public User() {

    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public User(String nom, String prenom, String img_user) {
        this.nom = nom;
        this.prenom = prenom;
        this.img_user = img_user;
    }

    public User(String nom, String img_user) {
        this.nom = nom;
        this.img_user = img_user;
    }

    public User(String cin, String nom, String prenom, String adresse, String login, String pass, String email, String role, String img_user, int numTelephone, String date_naissance) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.role = role;
        this.img_user = img_user;
        this.numTelephone = numTelephone;
        this.date_naissance = date_naissance;
    }

    public User(String cin, String nom, String prenom, String adresse, String login, String pass, String email, String role, String user_categorie, String date_naissance, String img_user, int numTelephone) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.role = role;
        this.user_categorie = user_categorie;
        this.date_naissance = date_naissance;
        this.img_user = img_user;
        this.numTelephone = numTelephone;
    }

    public String getUser_categorie() {
        return user_categorie;
    }

    public void setUser_categorie(String user_categorie) {
        this.user_categorie = user_categorie;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    @Override
    public String toString() {
        return "User{" +
                "cin=" + cin +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", img_user=" + img_user +
                ", numTelephone=" + numTelephone +
                '}';
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }

    public int getNumTelephone() {
        return numTelephone;
    }

    public void setNumTelephone(int numTelephone) {
        this.numTelephone = numTelephone;
    }
}
