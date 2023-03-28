package com.salikkim.seller.Helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController
{
   private static final String url="http://192.168.1.52/salikkim_store/Seller/";
   private static ApiController clientobject;
   private static Retrofit retrofit;

     ApiController()
     {
        retrofit=new Retrofit.Builder()
                     .baseUrl(url)
                     .addConverterFactory(GsonConverterFactory.create())
                     .build();
     }

     public static synchronized ApiController getInstance()
     {
          if(clientobject==null)
              clientobject=new ApiController();
          return clientobject;
     }

     public ApiSet getApi()
     {
         return retrofit.create(ApiSet.class);
     }
}
