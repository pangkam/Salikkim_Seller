package com.salikkim.seller.Helper;

import com.salikkim.seller.Models.Address;
import com.salikkim.seller.Models.Images;
import com.salikkim.seller.Models.Orders;
import com.salikkim.seller.Models.Product;
import com.salikkim.seller.Models.ResponseModel;
import com.salikkim.seller.Models.Seller;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiSet {



    @GET("addresses.php")
    Call<List<Address>> getAddresses();

    @GET("profile.php")
    Call<List<Seller>> getSellerProfile(@Query("seller_id") String seller_id);

    @GET("products.php")
    Call<List<Product>> getProducts(@Query("seller_id") String seller_id);

    @GET("orders.php")
    Call<List<Orders>> getOrders(@Query("seller_id") String seller_id);


    @GET("images.php")
    Call<List<Images>> getImages(@Query("product_id") String product_id);


    @GET("setsellerprofile.php")
    Call<ResponseModel> setSellerProfile(
            @Query("seller_id") String user_id,
            @Query("name") String user_name,
            @Query("mobile") String phone,
            @Query("alt_mobile") String elt_phone,
            @Query("email") String email,
            @Query("address") String address);

    @Multipart
    @POST("addimage.php")
    Call<ResponseModel> addImage(@Part MultipartBody.Part image,
                                 @Part("product_id") RequestBody product_id);

    @GET("addproduct.php")
    Call<ResponseModel> addProduct(
            @Query("seller_id") String seller_id,
            @Query("title") String title,
            @Query("price") double price,
            @Query("sale_price") double sale_price,
            @Query("color") String color,
            @Query("size") String size,
            @Query("quantity") int quantity,
            @Query("desc") String desc,
            @Query("tags") String tags,
            @Query("address") String address);


    @GET("deleteorder.php")
    Call<ResponseModel> deleteOrder(
            @Query("order_id") int order_id);

    @GET("deleteproduct.php")
    Call<ResponseModel> deleteProduct(
            @Query("product_id") int id);

    @GET("deleteimage.php")
    Call<ResponseModel> deleteImage(
            @Query("image_id") int image_id,
            @Query("image_name") String img_name);

    @GET("updateproduct.php")
    Call<ResponseModel> updateProduct(
            @Query("product_id") int product_id,
            @Query("title") String title,
            @Query("price") double price,
            @Query("sale_price") double sale_price,
            @Query("color") String color,
            @Query("size") String size,
            @Query("quantity") int quantity,
            @Query("desc") String desc,
            @Query("tags") String tags);

    @GET("updateaddress.php")
    Call<ResponseModel> updateAddress(
            @Query("product_id") int product_id,
            @Query("address") String address);

}
