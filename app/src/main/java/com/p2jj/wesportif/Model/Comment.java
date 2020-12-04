package com.p2jj.wesportif.Model;

public class Comment {


    private String id_User, NomUser,prenomUser, Comment,dateComment ,userImg_com;
    private int id_Event, Idcomment, img_user_commenter;

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getUserImg_com() {
        return userImg_com;
    }

    public void setUserImg_com(String userImg_com) {
        this.userImg_com = userImg_com;
    }

    public Comment(String id_User, String nomUser, String comment, String dateComment, int id_Event, int idcomment) {
        this.id_User = id_User;
        NomUser = nomUser;
        Comment = comment;
        this.dateComment = dateComment;
        this.id_Event = id_Event;
        Idcomment = idcomment;
    }

    public Comment() {

    }

    public Comment(String nomUser, String comment, String dateComment, int img_user_commenter) {
        NomUser = nomUser;
        Comment = comment;
        this.dateComment = dateComment;
        this.img_user_commenter = img_user_commenter;
    }

    public int getImg_user_commenter() {
        return img_user_commenter;
    }

    public void setImg_user_commenter(int img_user_commenter) {
        this.img_user_commenter = img_user_commenter;
    }

    public String getId_User() {
        return id_User;
    }

    public void setId_User(String id_User) {
        this.id_User = id_User;
    }

    public String getNomUser() {
        return NomUser;
    }

    public void setNomUser(String nomUser) {
        NomUser = nomUser;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public int getId_Event() {
        return id_Event;
    }

    public void setId_Event(int id_Event) {
        this.id_Event = id_Event;
    }

    public int getIdcomment() {
        return Idcomment;
    }

    public void setIdcomment(int idcomment) {
        Idcomment = idcomment;
    }
}
