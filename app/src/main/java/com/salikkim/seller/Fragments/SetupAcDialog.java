package com.salikkim.seller.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.salikkim.seller.Activities.MainActivity;
import com.salikkim.seller.Helper.ApiController;
import com.salikkim.seller.Models.ResponseModel;
import com.salikkim.seller.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SetupAcDialog extends Dialog implements View.OnClickListener, View.OnTouchListener {

    private TextInputEditText ti_name;
    private TextInputEditText ti_mobile;
    private TextInputEditText ti_alt_mobile;
    private TextInputEditText ti_email;
    private TextInputEditText ti_address;
    private TextInputLayout tl_name;
    private TextInputLayout tl_mobile;

    private TextInputLayout tl_alt_mobile;
    private TextInputLayout tl_email;
    private TextInputLayout tl_address;
    private TextView btn_save;
    private ProgressBar progressBar;
    private String MY_PREFS_NAME = "Seller";
    private boolean set;
    private String pref_user_id;

    private ImageView btn_close;


    public SetupAcDialog(@NonNull Context activity, boolean set) {
        super(activity);
        this.set = set;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_setup_dialog);

        SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        pref_user_id = prefs.getString("seller_id", null);
        String pref_user_name = prefs.getString("seller_name", null);
        String pref_mobile = prefs.getString("mobile", null);
        String pref_alt_mobile = prefs.getString("alt_mobile", null);
        String pref_email = prefs.getString("email", null);
        String pref_address_name = prefs.getString("address_name", null);


        btn_close = findViewById(R.id.btn_close_ac_dialog);
        btn_save = findViewById(R.id.btn_save_ac_dialog);
        progressBar = findViewById(R.id.progress_ac_dialog);

        tl_name = findViewById(R.id.tl_name_dialog);
        ti_name = findViewById(R.id.ti_Name_dialog);

        tl_mobile = findViewById(R.id.tl_mobile_dialog);
        ti_mobile = findViewById(R.id.ti_mobile_dialog);

        // tl_alt_mobile = findViewById(R.id.tl_alt_mobile_dialog);
        ti_alt_mobile = findViewById(R.id.ti_alt_number_dialog);

        // tl_email = findViewById(R.id.tl_email_dialog);
        ti_email = findViewById(R.id.ti_email_dialog);

        tl_address = findViewById(R.id.tl_address_dialog);
        ti_address = findViewById(R.id.ti_address_dialog);

        ti_name.setText(pref_user_name);
        ti_mobile.setText(pref_mobile);
        ti_alt_mobile.setText(pref_alt_mobile);
        ti_email.setText(pref_email);
        ti_address.setText(pref_address_name);

        btn_save.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        ti_name.setOnTouchListener(this);
        ti_mobile.setOnTouchListener(this);
        ti_address.setOnTouchListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close_ac_dialog:
                if (set) {
                    ((Activity) getContext()).finish();
                } else {
                    dismiss();
                }
                break;
            case R.id.btn_save_ac_dialog:
                getTexts();
                break;
        }
    }

    private void getTexts() {
        if (ti_name.getText().toString().isEmpty()) {
            tl_name.setError(" Seller Name cannot be blank");
        } else if (ti_mobile.getText().toString().isEmpty()) {
            tl_mobile.setError("Enter mobile number registered with WhatsApp");
        } else if (ti_address.getText().toString().isEmpty()) {
            tl_address.setError("Enter full address detail");
        } else {
            setProfile(pref_user_id,
                    ti_name.getText().toString().trim(),
                    ti_mobile.getText().toString().trim(),
                    ti_alt_mobile.getText().toString().trim(),
                    ti_email.getText().toString().trim(),
                    ti_address.getText().toString().trim());
        }
    }

    private void setProfile(String seller_id, String name, String mobile, String alt_mobile, String email, String address) {
        btn_save.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Call<ResponseModel> call = ApiController.getInstance().getApi().setSellerProfile(
                seller_id, name, mobile, alt_mobile, email, address);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        SharedPreferences.Editor editor = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("seller_id", seller_id);
                        editor.putString("seller_name", name);
                        editor.putString("mobile", mobile);
                        editor.putString("alt_mobile", alt_mobile);
                        editor.putString("email", email);
                        editor.putString("address_name", address);
                        editor.apply();
                        new AlertDialog.Builder(getContext()).setMessage("Profile Saved Successfully").setCancelable(false)
                                .setPositiveButton("OKAY", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getContext().startActivity(intent);
                                        dismiss();
                                    }
                                }).show();
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    btn_save.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                btn_save.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ti_Name_dialog:
                tl_name.setErrorEnabled(false);
                break;
            case R.id.ti_mobile_dialog:
                tl_mobile.setErrorEnabled(false);
                break;
            case R.id.ti_address_dialog:
                tl_address.setErrorEnabled(false);
                break;
        }
        return false;
    }

}
