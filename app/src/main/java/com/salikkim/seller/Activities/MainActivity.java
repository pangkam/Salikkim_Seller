package com.salikkim.seller.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.salikkim.seller.Fragments.SetupAcDialog;
import com.salikkim.seller.R;
import com.salikkim.seller.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding mainBinding;
    private String MY_PREFS_NAME = "Seller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);
        mainBinding.btnProductMain.setOnClickListener(this);
        mainBinding.btnOrdersMain.setOnClickListener(this);
        mainBinding.btnRatingsMain.setOnClickListener(this);
        mainBinding.btnPaymentsMain.setOnClickListener(this);
        mainBinding.btnPrivacyMain.setOnClickListener(this);
        mainBinding.btnHelpMain.setOnClickListener(this);
        mainBinding.btnLogoutMain.setOnClickListener(this);
        mainBinding.btnEditProfile.setOnClickListener(this);
        setViews();
    }

    private void setViews() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String pref_user_name = prefs.getString("seller_name", null);
        String pref_mobile = prefs.getString("mobile", null);
        String pref_alt_mobile = prefs.getString("alt_mobile", null);
        String pref_email = prefs.getString("email", null);
        String pref_address_name = prefs.getString("address_name", null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Mobile: " + pref_mobile);
        if (pref_alt_mobile != null)
            stringBuilder.append("\nAlt_Mobile: " + pref_alt_mobile);

        if (pref_email != null)
            stringBuilder.append("\nEmail: " + pref_email);

        if (pref_address_name != null)
            stringBuilder.append("\nAddress: " + pref_address_name);
        mainBinding.tvProfileName.setText(pref_user_name);
        mainBinding.tvProfileContacts.setText(stringBuilder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_product_main:
                startActivity(new Intent(MainActivity.this, ProductActivity.class)
                        .putExtra("seller_id", "7005643266"));
                break;
            case R.id.btn_orders_main:
                startActivity(new Intent(MainActivity.this, OrdersActivity.class)
                        .putExtra("seller_id", "7005643266"));
                break;
            case R.id.btn_edit_profile:
                editProfile();
                break;
        }
    }

    private void editProfile() {
        SetupAcDialog setupAcDialog = new SetupAcDialog(MainActivity.this, false);
        setupAcDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setupAcDialog.setCancelable(false);
        setupAcDialog.show();
        Window window = setupAcDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}