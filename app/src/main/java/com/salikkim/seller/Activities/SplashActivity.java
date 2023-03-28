package com.salikkim.seller.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.salikkim.seller.Fragments.SetupAcDialog;
import com.salikkim.seller.Helper.ApiController;
import com.salikkim.seller.Models.Seller;
import com.salikkim.seller.databinding.ActivitySplashBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding splashBinding;
    private String seller_id;
    private SetupAcDialog setupAcDialog;
    private String MY_PREFS_NAME = "Seller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = splashBinding.getRoot();
        setContentView(view);
        seller_id = "7005643266";
        getSellerProfile(seller_id);
       /* if (seller_id != null) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("seller_id", seller_id);
            startActivity(intent);
        } else {
            setupAcDialog = new SetupAcDialog(SplashActivity.this, true);
            setupAcDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setupAcDialog.setCancelable(true);
            setupAcDialog.show();
            Window window = setupAcDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }*/
    }

    private void getSellerProfile(String s) {
        Call<List<Seller>> call = ApiController.getInstance().getApi().getSellerProfile(s);
        call.enqueue(new Callback<List<Seller>>() {
            @Override
            public void onResponse(Call<List<Seller>> call, Response<List<Seller>> response) {
                if (response.body() != null) {
                    setSharePrefs(response.body().get(0).getS_Id(),
                            response.body().get(0).getName(),
                            response.body().get(0).getMobile(),
                            response.body().get(0).getAlt_Mobile(),
                            response.body().get(0).getEmail(),
                            response.body().get(0).getAddress(),
                            response.body().get(0).getAccounts());

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("seller_id", seller_id);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Seller>> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setSharePrefs(String s_id, String name, String mobile, String alt_mobile, String email, String address_name,String accounts) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("seller_id", s_id);
        editor.putString("seller_name", name);
        editor.putString("mobile", mobile);
        editor.putString("alt_mobile", alt_mobile);
        editor.putString("email", email);
        editor.putString("address_name", address_name);
        editor.putString("accounts", accounts);
        editor.apply();
    }
}