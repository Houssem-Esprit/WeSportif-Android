package com.p2jj.wesportif.Data_Managers;

import android.graphics.Bitmap;



import org.json.JSONArray;

import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface API_Service_node {

    @FormUrlEncoded
    @POST("register")
    Call<Object> Signup(
            @Field("cin") String cin,
            @Field("nom") String nom,
            @Field("prenom") String prenom,
            @Field("login") String login,
            @Field("pass") String pass,
            @Field("email") String email,
            @Field("role") int role,
            @Field("img") String img_user,
            @Field("numTel") int numTelephone,
            @Field("date_naissance") String date_naissance,
            @Field("cat") JSONArray user_categorie

    );

    @FormUrlEncoded
    @POST("login")
    Call<Object> Login(
            @Field("login") String login,
            @Field("pass") String pass
    );

    @FormUrlEncoded
    @POST("getuserdetails")
    Call<Object> GetUserDetails(
            @Field("cin") String cin
    );

    @FormUrlEncoded
    @POST("addevent")
    Call<Object> AddEvent(
            @Field("titre") String titre,
            @Field("desc") String desc,
            @Field("capacite") int cap,
            @Field("lieu") String lieu,
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("dateDeb_event") String dateDeb,
            @Field("dateFin_event") String dateFin,
            @Field("heureDeb_event") String heureDeb,
            @Field("heureFin_event") String heureFin,
            @Field("cat") int cat,
            @Field("img") String img,
            @Field("e_admin") String e_admin
    );

    @FormUrlEncoded
    @POST("getuserevents")
    Call<Object> GetUserEvents(
            @Field("cin") String cin
    );

    @FormUrlEncoded
    @POST("geteventsbycat")
    Call<Object> GetUserEventsByCat(
            @Field("cin") String cin
    );

    @GET("getevents")
    Call<Object> GetEvents(
    );

    @GET("getnearevents")
    Call<Object> GetNearEvents(
    );


    @FormUrlEncoded
    @POST("addimc")
    Call<Object> addImc(
            @Field("height") float height,
            @Field("weight") float weight,
            @Field("imc") float imc,
            @Field("cin") String cin
    );


    @GET("getcoachs")
    Call<Object> GetCoachs(
    );

    @Multipart
    @POST("/uploadEventImg/{id}")
    Call<ResponseBody> uploadImageEvent(@Part MultipartBody.Part image, @Part("upload") RequestBody name, @Path("id") int id);

    @Multipart
    @POST("/uploadUserImg/{id}")
    Call<ResponseBody> uploadImageUser(@Part MultipartBody.Part image, @Part("upload") RequestBody name, @Path("id") String id);

    @Multipart
    @POST("/uploadUserCover/{id}")
    Call<ResponseBody> uploadCoverUser(@Part MultipartBody.Part image, @Part("upload") RequestBody name, @Path("id") String id);

    @GET("getcategories")
    Call<Object> GetCats(
    );


    @FormUrlEncoded
    @POST("updateuser")
    Call<Object> UpdateUser(
            @Field("cin") String cin,
            @Field("email") String email,
            @Field("numTel") String numTel,
            @Field("date_naissance") String ddn
    );
    @FormUrlEncoded
    @POST("geteventdetails")
    Call<Object> GetEventDetails(
            @Field("eventId") int eventId
    );
    @FormUrlEncoded
    @POST("joinevent")
    Call<Object> ParticiperEvent(
            @Field("cin") String cin,
            @Field("eventId") int eventId
    );
    @FormUrlEncoded
    @POST("checkparticipation")
    Call<Object> CheckParticipation(
            @Field("cin") String cin,
            @Field("eventId") int eventId
    );
    @FormUrlEncoded
    @POST("delparticipation")
    Call<Object> DelParticipation(
            @Field("cin") String cin,
            @Field("eventId") int eventId
    );

    @FormUrlEncoded
    @POST("geteventreactions")
    Call<Object> GetComments(
            @Field("eventId") int eventId
    );

    @FormUrlEncoded
    @POST("likecount")
    Call<Object> CountLike(
            @Field("eventId") int eventId
    );
    @FormUrlEncoded
    @POST("comcount")
    Call<Object> CountCom(
            @Field("eventId") int eventId
    );
    @FormUrlEncoded
    @POST("addcomment")
    Call<Object> PostCom(
            @Field("eventId") int eventId,
            @Field("cin") String cin,
            @Field("comment") String com

    );

}
