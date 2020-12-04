package com.p2jj.wesportif.Data_Managers;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // private  static  final String BASE_URL="http://wesportiftif.eu-4.evennode.com/";
  private  static  final String BASE_URL="http://wessporitf.eu-4.evennode.com/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if (mInstance==null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public API_Service_node getApi_service_node(){
        return retrofit.create(API_Service_node.class);
    }

}
